package ba.ekapic1.stonebase.filter;

import ba.ekapic1.stonebase.model.Field;

import java.util.Optional;
import java.util.function.BiFunction;

@SuppressWarnings("unchecked")
public interface FilterBuilderComponent<SELF extends FilterBuilderComponent<SELF>> {
    default ConditionBuilder<SELF> and() {
        return (ConditionBuilder<SELF>) ConditionBuilder.and(this::with, this);
    }

    default ConditionBuilder<SELF> or() {
        return (ConditionBuilder<SELF>) ConditionBuilder.or(this::with, this);
    }

    SELF with(final Condition condition);

    default SELF with(final Field field, final ConditionType type, final Object value) {
        return with(new Condition(field, type, value));
    }

    default <V> SELF optionally(final Optional<V> value, final BiFunction<? super V, ? super SELF, ? extends SELF> op) {
        if (value.isPresent()) {
            return op.apply(value.get(), (SELF) this);
        }

        return (SELF) this;
    }

    default SELF optionally(final Field field, final ConditionType type, final Optional<?> optionalValue) {
        return optionally(optionalValue, (value, builder) -> builder.with(field, type, value));
    }
}
