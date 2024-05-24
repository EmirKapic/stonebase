package ba.ekapic1.stonebase.filter;

import ba.ekapic1.stonebase.model.Field;
import org.springframework.data.jpa.domain.Specification;

public interface FilterBuilder<T> extends FilterBuilderComponent<FilterBuilder<T>>{

    static <T> FilterBuilder<T> simple() {
        return new SimpleFilterBuilder<>();
    }

    /**
     * Allows users to pass in a function using which they can mutate the existing builder instance. Side-effects are expected.
     * @param customizer - function that takes a builder instance to modify.
     * @return - the same builder instance after being modified by the function.
     */
    default FilterBuilder<T> with(final FilterBuilderCustomizer<T> customizer) {
        return customizer.apply(this);
    }

    FilterBuilder<T> with(final Order<T> order);

    default FilterBuilder<T> withOrder(final Field field, final boolean asc) {
        return with(Order.of(field, asc));
    }

    default FilterBuilder<T> withOrderAsc(final Field field) {
        return with(Order.asc(field));
    }

    default FilterBuilder<T> withOrderDesc(final Field field) {
        return with(Order.desc(field));
    }

    Specification<T> build();
}
