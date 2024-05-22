package ba.ekapic1.stonebase.filter;

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

    Specification<T> build();
}
