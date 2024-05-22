package ba.ekapic1.stonebase.filter;

import ba.ekapic1.stonebase.model.Field;

import java.util.List;

public record Condition(Field field, ConditionType type, Object value) {
    public Condition(final ConditionType type, final Condition... nestedConditions) {
        this(null, type, nestedConditions);
    }

    public Condition[] getConditions() {
        if (!type.isAggregate()) {
            throw new IllegalStateException("Detected attempt to get nested conditions of a non-aggregate condition.");
        }

        return (Condition[]) value;
    }
}
