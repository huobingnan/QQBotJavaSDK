package buddha.qqbot;

import java.util.*;

public abstract class BaseStrategyRegistry<C, S extends IStrategy<C>> {
    protected Map<C, List<S>> registry = new HashMap<>();

    public List<S> dispatch(C condition) {
        final List<S> strategies = registry.get(condition);
        if (strategies == null || strategies.isEmpty()) {
            final S defaultStrategy = onConditionMismatch(condition);
            return defaultStrategy == null ?
                    Collections.emptyList() : Collections.singletonList(defaultStrategy);
        } else {
            return strategies;
        }
    }
    public void register(C condition, S strategy) {
        Objects.requireNonNull(condition, "condition can't be null");
        Objects.requireNonNull(strategy, "strategy can't be null");
        if (!registry.containsKey(condition)) {
            final List<S> listeners = new ArrayList<>();
            listeners.add(strategy);
            registry.put(condition, listeners);
        } else {
            registry.get(condition).add(strategy);
        }
    }

    abstract S onConditionMismatch(C condition);
}
