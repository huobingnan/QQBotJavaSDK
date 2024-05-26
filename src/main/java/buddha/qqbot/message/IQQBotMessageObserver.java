package buddha.qqbot.message;

import buddha.qqbot.IStrategy;
import buddha.qqbot.QQBot;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.WebSocket;

public interface IQQBotMessageObserver extends IStrategy<Integer> {

    void observer(JSONObject message, WebSocket socket, QQBot botInstance);

    default String name() { return "QQBotMessageObserver"; }
}
