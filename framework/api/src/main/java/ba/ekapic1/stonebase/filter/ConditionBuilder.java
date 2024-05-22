package ba.ekapic1.stonebase.filter;

import java.util.function.Consumer;

public interface ConditionBuilder<T> extends FilterBuilderComponent<ConditionBuilder<T>> {
    static <T> ConditionBuilder<T> and(final Consumer<? super Condition> conditionConsumer, final T returnTo) {
        return new SimpleConditionBuilder<>(conditionConsumer, ConditionType.AND, returnTo);
    }

    static <T> ConditionBuilder<T> or(final Consumer<? super Condition> conditionConsumer, final T returnTo) {
        return new SimpleConditionBuilder<>(conditionConsumer, ConditionType.OR, returnTo);
    }

    T end();
}
