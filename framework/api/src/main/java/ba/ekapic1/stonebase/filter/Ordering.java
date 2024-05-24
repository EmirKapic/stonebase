package ba.ekapic1.stonebase.filter;

import ba.ekapic1.stonebase.utils.CriteriaUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

public class Ordering<T> implements Specification<T> {
    private final Order<T> order;

    public Ordering(final Order<T> order) {
        this.order = order;
    }

    @Override
    public Predicate toPredicate(@NonNull final Root<T> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        final CriteriaUtils<T> utils = CriteriaUtils.of(root, (CriteriaQuery<T>) query, criteriaBuilder);

        return utils.createOrderPredicate(order);
    }
}
