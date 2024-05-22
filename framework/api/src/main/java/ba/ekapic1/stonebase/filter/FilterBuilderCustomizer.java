package ba.ekapic1.stonebase.filter;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface FilterBuilderCustomizer<T> extends UnaryOperator<FilterBuilder<T>> {
}
