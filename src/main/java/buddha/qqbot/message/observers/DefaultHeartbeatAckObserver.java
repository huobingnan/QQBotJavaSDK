package buddha.qqbot.message.observers;

import buddha.qqbot.QQBot;
import buddha.qqbot.message.IQQBotMessageObserver;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;

/**
 * 默认心跳消息监听类，其功能主要有
 *  1. 接受心跳消息，并通过日志输出
 */
@Slf4j
public class DefaultHeartbeatAckObserver implements IQQBotMessageObserver {
    @Override
    public void observer(JSONObject message, WebSocket socket, QQBot botInstance) {
        log.info("接收到心跳ACK消息： {}", message);
    }


    @Override
    public String name() { return "默认心跳ACK监听器"; }

    @Override
    public Integer getCondition() { return 11; }
}
