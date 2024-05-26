package buddha.qqbot;

import buddha.qqbot.message.IQQBotMessageObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpcodeRegistry extends BaseStrategyRegistry<Integer, IQQBotMessageObserver> {
    @Override
    IQQBotMessageObserver onConditionMismatch(Integer condition) {
        log.warn("不能有效处理OP： {}", condition);
        return null;
    }
}
