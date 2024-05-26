package buddha.qqbot;

import okhttp3.WebSocket;

public interface IEventTypeObserver extends IStrategy<EventTypeEnum> {

    void observer(WebSocket socket, QQBot bot);
}
