package buddha.qqbot.message.observers;

import buddha.qqbot.QQBot;
import buddha.qqbot.message.IQQBotMessageObserver;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;

@Slf4j
public class DefaultDispatchObserver implements IQQBotMessageObserver {
    @Override
    public void observer(JSONObject message, WebSocket socket, QQBot botInstance) {
        log.info("消息类型： {}, 消息内容： {}", message.get("t"), message.get("d"));
        botInstance.resetLatestMessage(message);
    }


    @Override
    public String name() { return "默认消息派发监听器"; }

    @Override
    public Integer getCondition() { return  0; }
}
