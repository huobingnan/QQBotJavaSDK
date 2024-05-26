package buddha.qqbot.message;

import buddha.qqbot.OpcodeRegistry;
import buddha.qqbot.QQBot;
import buddha.qqbot.QQBotProperties;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class QQBotMessageDispatcher extends WebSocketListener implements AutoCloseable {
    private final QQBot botInstance;

    private final OpcodeRegistry registry = new OpcodeRegistry();
    private ExecutorService dispatcherPool;


    public QQBotMessageDispatcher(final QQBot bot) {
        botInstance = bot;
        createDispatcherPool(bot.getQQBotProps());
    }

    /**
     * 创建消息派发的线程池
     */
    private void createDispatcherPool(final QQBotProperties props) {
        final int size = Integer.parseInt(props.getProperty("qqbot.message.pool.size", "10"));
        dispatcherPool = new ThreadPoolExecutor(
                size,
                size,
                0L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                (task) -> {
                    final String uid = UUID.randomUUID().toString().replace("-", "");
                    return new Thread(botInstance.getMessageThreadGroup(), task, "message-thread-"+uid);
                }
        );
    }


    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.warn("WEBSOCKET链接被关闭： {}", reason);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.warn("WEBSOCKET连接将被关闭： CODE: {}, REASON: {}", code, reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        log.error("WEBSOCKET连接错误： {}", t.getMessage());
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        // 接受并派发消息
        log.debug("接收到WEBSOCKET消息： {}", text);
        final JSONObject payload = JSON.parseObject(text);
        final Integer opcode = payload.getInteger("op");
        // dispatch
        registry.dispatch(opcode)
                .stream()
                .<Runnable>map(it -> () -> it.observer(payload, webSocket, botInstance))
                .forEach(dispatcherPool::submit);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        log.info("与WSS_GATEWAY成功建立连接...");
        // 注册监听器
        final ServiceLoader<IQQBotMessageObserver> loader = ServiceLoader.load(IQQBotMessageObserver.class);
        for (final IQQBotMessageObserver observer : loader) {
            log.info("发现消息监听器： {}", observer.name());
            registry.register(observer.getCondition(), observer);
        }

    }

    @Override
    public void close() throws Exception {
        dispatcherPool.shutdownNow();
    }

}
