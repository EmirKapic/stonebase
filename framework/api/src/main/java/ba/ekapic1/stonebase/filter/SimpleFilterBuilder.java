package ba.ekapic1.stonebase.filter;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class SimpleFilterBuilder<T> implements FilterBuilder<T> {
    private final List<Filter<T>> filters;
    private final List<Ordering<T>> orders;

    SimpleFilterBuilder() {
        this.filters = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    @Override
    public FilterBuilder<T> with(final Condition condition) {
        this.filters.add(new Filter<>(condition));
        return this;
    }


    @Override
    public FilterBuilder<T> with(final Order<T> order) {
        this.orders.add(new Ordering<>(order));
        return this;
    }

    @Override
    public Specification<T> build() {
        // TODO: What if only sorted, and no filter?
        if (filters.isEmpty()) {
            return Specification.where(null);
        }

        Specification<T> finalSpec = Specification.where(filters.get(0));

        for (int i = 1; i < filters.size(); i++) {
            finalSpec = Specification.where(finalSpec).and(filters.get(i));
        }

        if (orders.isEmpty()) {
            return finalSpec;
        }

        for (final Ordering<T> order : orders) {
            finalSpec = Specification.where(finalSpec)
                    .and(order);
        }

        return finalSpec;
    }
}
