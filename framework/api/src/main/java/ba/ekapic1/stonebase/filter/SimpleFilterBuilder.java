package ba.ekapic1.stonebase.filter;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class SimpleFilterBuilder<T> implements FilterBuilder<T> {
    private final List<Filter<T>> filters;

    SimpleFilterBuilder() {
        this.filters = new ArrayList<>();
    }

    @Override
    public FilterBuilder<T> with(final Condition condition) {
        this.filters.add(new Filter<>(condition));
        return this;
    }


    @Override
    public Specification<T> build() {
        if (filters.isEmpty()) {
            return Specification.where(null);
        }

        Specification<T> finalSpec = Specification.where(filters.get(0));

        for (int i = 1; i < filters.size(); i++) {
            finalSpec = Specification.where(finalSpec).and(filters.get(i));
        }

        return finalSpec;
    }
}
