package ba.ekapic1.stonebase.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleConditionBuilder<T> implements ConditionBuilder<T> {
    private final Consumer<? super Condition> conditionConsumer;
    private final List<Condition> conditions;
    private final ConditionType type;
    private final T returnTo;

    SimpleConditionBuilder(final Consumer<? super Condition> conditionConsumer, final ConditionType type, final T returnTo) {
        this.conditionConsumer = conditionConsumer;
        this.conditions = new ArrayList<>();
        this.type = type;
        this.returnTo = returnTo;
    }

    @Override
    public ConditionBuilder<T> with(final Condition condition) {
        this.conditions.add(condition);
        return this;
    }

    @Override
    public T end() {
        conditionConsumer.accept(new Condition(type, conditions.toArray(new Condition[0])));
        return returnTo;
    }
}
