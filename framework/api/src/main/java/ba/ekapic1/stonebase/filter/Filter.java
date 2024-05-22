package ba.ekapic1.stonebase.filter;

import ba.ekapic1.stonebase.utils.CriteriaUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;


public class Filter<T> implements Specification<T> {
    private final Condition condition;

    public Filter(final Condition condition) {
        this.condition = condition;
    }

    @Override
    public Predicate toPredicate(@NonNull final Root<T> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        final CriteriaUtils<T> utils = CriteriaUtils.of(root, criteriaBuilder);
        return utils.createPredicate(this.condition);
    }
}
