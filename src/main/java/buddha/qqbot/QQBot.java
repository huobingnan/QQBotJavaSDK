package buddha.qqbot;

import buddha.qqbot.message.QQBotMessageDispatcher;
import buddha.qqbot.openapi.CommonRequest;
import buddha.qqbot.openapi.IOkHttpRequestType;
import buddha.qqbot.openapi.pojo.AccessToken;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class QQBot {
    /** QQBot配置信息 */
    private QQBotProperties botProps;
    private OkHttpClient client;
    /** WEBSOCKET消息派发器 */
    private QQBotMessageDispatcher dispatcher;
    /** 心跳调度器 */
    private final ScheduledExecutorService heartbeatScheduler =
            new ScheduledThreadPoolExecutor(1, r -> new Thread(r, "heartbeat-thread"));

    /** 最新的消息： 用户心跳包的发送 */
    @Getter
    private JSONObject latestMessage = null;

    /** 建立好的Websocket连接 */
    private WebSocket wssConnection;

    /** 消息处理线程组 */
    @Getter
    private final ThreadGroup messageThreadGroup = new ThreadGroup("MESSAGE_THREAD_GROUP");

    private AccessToken token;

    public QQBot() { }

    private void initOkHttpClient() {
        client = new OkHttpClient.Builder()
                .build();
    }


    public QQBotProperties getQQBotProps() { return botProps; }

    public void launch(String... args) {
        // 初始化QQBot
        // 1. 加载配置文件
        botProps = new QQBotProperties();
        log.info("QQBot配置文件加载完成...");
        // 2. 初始化OkHttpClient
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        initOkHttpClient();
        log.info("OkHttpClient初始化完成...");
        // 3. 初始化WEBSOCKET消息派发器
        dispatcher = new QQBotMessageDispatcher(this);
        log.info("WEBSOCKET消息派发器初始化完成...");
        // 请求ACCESS TOKEN
        latestToken();
        log.info("成功获取ACCESS_TOKEN...");
        // 请求WSS网关地址
        String wssGatewayUri = "";
        try (final Response rsp = CommonRequest.WSS_GATEWAY.send(
                client,
                token.getToken(),
                botProps.getProperty("qqbot.appId"),
                false)) {
            if (!rsp.isSuccessful() || rsp.body() == null) throw new RuntimeException("请求WSS_GATEWAY失败");
            wssGatewayUri = JSON.parseObject(rsp.body().string()).getString("url");
            log.info("WSS_GATEWAY_URL -> {}", wssGatewayUri);
        } catch (IOException ex) {
            throw new RuntimeException("请求WSS_GATEWAY失败", ex);
        }
        final Request wssGatewayConn = CommonRequest.WSS_GATEWAY_CONNECT.build(wssGatewayUri);
        wssConnection = client.newWebSocket(wssGatewayConn, dispatcher);
    }

    public String latestToken() {
        if (token == null || token.isExpired()) {
            // 获取Token
            try (final Response rsp =
                         post(CommonRequest.ACCESS_TOKEN, botProps.get("qqbot.appId"), botProps.get("qqbot.secret"))) {
                final ResponseBody body = rsp.body();
                if (body == null) throw new RuntimeException("获取ACCESS TOKEN失败");
                token = JSON.parseObject(body.string(), AccessToken.class);
                return token.getToken();
            } catch (IOException e) {
                throw new RuntimeException("获取ACCESS TOKEN失败", e);
            }
        }
        return token.getToken();
    }

    public void resetLatestMessage(JSONObject message) { latestMessage = message; }

    public void registerHeartbeat(long timeout) {
        heartbeatScheduler.scheduleAtFixedRate(() -> {
            final JSONObject ack = new JSONObject();
            ack.put("op", 1);
            ack.put("d", latestMessage);
            final String ackStr = ack.toJSONString();
            log.info("发送心跳包： {}", ackStr);
            wssConnection.send(ackStr);
        }, timeout, timeout, TimeUnit.MILLISECONDS);
    }

    public Response post(IOkHttpRequestType request, Object... args) throws IOException {
        return request.send(client, args);
    }

}
