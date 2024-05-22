package ba.ekapic1.stonebase.filter;

public enum ConditionType {
    EQUAL(false),
    NOT_EQUAL(false),
    GREATER_THAN(false),
    GREATER_THAN_EQUAL(false),
    LESS_THAN(false),
    LESS_THAN_EQUAL(false),
    IN(false),
    NOT_IN(false),
    LIKE(false),
    LIKE_IGNORE_CASE(false),
    AND(true),
    OR(true);

    private final boolean aggregate;

    ConditionType(final boolean aggregate) {
        this.aggregate = aggregate;
    }

    public boolean isAggregate() {
        return aggregate;
    }
}
