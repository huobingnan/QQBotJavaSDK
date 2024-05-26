package buddha.qqbot.message.observers;

import buddha.qqbot.QQBot;
import buddha.qqbot.message.IQQBotMessageObserver;
import buddha.qqbot.message.Intents;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;

import java.util.Arrays;

@Slf4j
public class DefaultIdentifyObserver implements IQQBotMessageObserver {

    @Override
    public void observer(JSONObject message, WebSocket socket, QQBot botInstance) {
        final Long interval = message.getJSONObject("d").getLong("heartbeat_interval");
        log.info("服务端要求心跳频率：{} ms", interval-100);
        botInstance.registerHeartbeat(interval);
        final JSONObject identifyPayload = new JSONObject();
        identifyPayload.put("op", 2);
        identifyPayload.putObject("d");
        final JSONObject data = identifyPayload.getJSONObject("d");
        data.put("token", "QQBot " + botInstance.latestToken());
        data.put("intents", findIntentsOrDefault(botInstance));
        data.put("shared", findSharedOrDefault(botInstance));
        socket.send(identifyPayload.toJSONString());
    }

    private Integer findIntentsOrDefault(QQBot bot) {
        final String intents = bot.getQQBotProps().getProperty("qqbot.message.intents");
        log.info("使用配置文件中【qqbot.message.intents】的配置： {}", intents);
        if (intents == null) {
            log.warn("未找到intents配置【qqbot.message.intents】，使用默认的配置: {} 可能会导致认证失败", Intents.publicIntents());
            return Intents.all();
        }
        return Integer.parseInt(intents);
    }


    private int[] findSharedOrDefault(final QQBot bot) {
        // 默认不分片
        final String shared =
                bot.getQQBotProps().getProperty("qqbot.message.shared", "0,1");
        log.info("分片模式： {}", shared);
        return Arrays.stream(shared.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    @Override
    public String name() { return "默认的身份验证监听器"; }

    @Override
    public Integer getCondition() { return 10; }
}
