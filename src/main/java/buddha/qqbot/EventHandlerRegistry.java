package buddha.qqbot;

/**
 * 事件处理器注册表
 */
public class EventHandlerRegistry extends BaseStrategyRegistry<EventTypeEnum, IEventTypeObserver> {

    @Override
    IEventTypeObserver onConditionMismatch(EventTypeEnum condition) {
        // TODO
        return null;
    }
}
