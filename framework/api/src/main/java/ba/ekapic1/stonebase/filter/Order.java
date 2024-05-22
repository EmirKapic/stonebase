package ba.ekapic1.stonebase.filter;

import ba.ekapic1.stonebase.model.Field;

// TODO: CompositeOrders
public class Order<T> {
    private final Field field;
    private final boolean asc;

    Order(final Field field, final boolean asc) {
        this.field = field;
        this.asc = asc;
    }

    public static <T> Order<T> asc(final Field field) {
        return new Order<>(field, true);
    }

    public static <T> Order<T> desc(final Field field) {
        return new Order<>(field, false);
    }

    public static <T> Order<T> of(final Field field, final boolean asc) {
        return new Order<>(field, asc);
    }

    public Field field() {
        return field;
    }

    public boolean isAsc() {
        return asc;
    }
}
