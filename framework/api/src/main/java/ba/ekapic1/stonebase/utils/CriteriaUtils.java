package ba.ekapic1.stonebase.utils;

import ba.ekapic1.stonebase.filter.Condition;
import ba.ekapic1.stonebase.filter.ConditionType;
import ba.ekapic1.stonebase.model.Field;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class CriteriaUtils<T> {
    private final Root<T> root;
    private final CriteriaBuilder criteriaBuilder;

    private CriteriaUtils(final Root<T> root, final CriteriaBuilder criteriaBuilder) {
        this.root = root;
        this.criteriaBuilder = criteriaBuilder;
    }

    public static <T> CriteriaUtils<T> of(final Root<T> root, final CriteriaBuilder criteriaBuilder) {
        return new CriteriaUtils<>(root, criteriaBuilder);
    }

    public Predicate createPredicate(final Condition condition) {
         return switch (condition.type()) {
            case EQUAL:
                {
                    if (condition.value() != null) {
                        final Object value = condition.value();

                        if (value instanceof Boolean booleanValue) {
                            yield booleanValue ?
                                    criteriaBuilder.isTrue((Expression<Boolean>) fieldPath(root, condition.field())) :
                                    criteriaBuilder.isFalse((Expression<Boolean>) fieldPath(root, condition.field()));
                        }

                        yield criteriaBuilder.equal(fieldPath(root, condition.field()), condition.value());
                    } else {
                        // Due to sql equality rules we have to check for == null specifically
                        yield criteriaBuilder.isNull(fieldPath(root, condition.field()));
                    }
                }
            case NOT_EQUAL:
                {
                    final Predicate notEquals = criteriaBuilder.not(createPredicate(new Condition(condition.field(), ConditionType.EQUAL, condition.value())));
                    yield criteriaBuilder.or(notEquals, criteriaBuilder.isNull(fieldPath(root, condition.field())));
                }
            case GREATER_THAN: yield criteriaBuilder.greaterThan((Expression<? extends Comparable>) fieldPath(root, condition.field()), (Comparable) condition.value());

            case GREATER_THAN_EQUAL: yield criteriaBuilder.greaterThanOrEqualTo((Expression<? extends Comparable>) fieldPath(root, condition.field()), (Comparable) condition.value());

            case LESS_THAN: yield criteriaBuilder.lessThan((Expression<? extends Comparable>) fieldPath(root, condition.field()), (Comparable) condition.value());

            case LESS_THAN_EQUAL: yield criteriaBuilder.lessThanOrEqualTo((Expression<? extends Comparable>) fieldPath(root, condition.field()), (Comparable) condition.value());

            case IN:
                {
                    final Object[] arrayValue = asArray(condition.value());

                    if (Arrays.asList(arrayValue).contains(null)) {
                        // We need to check if the element is null or inside the list. This is because if there is a null in the list and the
                        // given element is null, the criteria in operator will decide that it isn't equal to anything inside the list, but logically it should be.

                        final Object[] filteredArrayValue = Arrays.stream(arrayValue).filter(Objects::nonNull).toArray();

                        yield criteriaBuilder.or(
                                fieldPath(root, condition.field()).in(filteredArrayValue),
                                criteriaBuilder.isNull(fieldPath(root, condition.field()))
                        );
                    }

                    yield fieldPath(root, condition.field()).in(arrayValue);
                }

            case NOT_IN:
                 {
                     final Object[] arrayValue = asArray(condition.value());

                     if (Arrays.asList(arrayValue).contains(null)) {
                         final Object[] filteredArrayValue = Arrays.stream(arrayValue).filter(Objects::nonNull).toArray();

                         yield criteriaBuilder.or(
                                 criteriaBuilder.not(fieldPath(root, condition.field()).in(filteredArrayValue)),
                                 criteriaBuilder.isNotNull(fieldPath(root, condition.field()))
                         );
                     }

                     yield criteriaBuilder.not(fieldPath(root, condition.field()).in(arrayValue));
                 }
            case LIKE: yield criteriaBuilder.like((Expression<String>) fieldPath(root, condition.field()), (String) condition.value());

            case LIKE_IGNORE_CASE:
                 {
                     yield criteriaBuilder.like(
                             criteriaBuilder.lower(criteriaBuilder.toString((Expression<Character>) fieldPath(root, condition.field()))),
                             ((String) condition.value()).toLowerCase()
                     );
                 }

            case AND: yield criteriaBuilder.and(createAggregatePredicate(condition.getConditions()));

            case OR: yield criteriaBuilder.or(createAggregatePredicate(condition.getConditions()));
        };
    }

    public Predicate[] createAggregatePredicate(final Condition[] conditions) {
        return Arrays.stream(conditions)
                .map(this::createPredicate)
                .toArray(Predicate[]::new);
    }

    private Path<T> fieldPath(final Root<T> root, final Field field) {
        if (!field.isComposite()) {
            // Field isn't composite, we can simply return its name.
            return root.get(field.getFieldName());
        }

        final List<Field> nestedFields = field.components();

        // Proceed to iteratively call root.get, moving current root further into the hierarchy until we find
        // what we're looking for.

        Path<T> fieldPath = root.get(nestedFields.get(0).getFieldName());

        for (int i = 1; i < nestedFields.size(); i++) {
            fieldPath = fieldPath.get(nestedFields.get(i).getFieldName());
        }

        return fieldPath;
    }

    private static Object[] asArray(final Object value) {
        if (value == null) {
            return new Object[0];
        } else if (value.getClass().isArray()) {
            return (Object[]) value;
        } else if (value instanceof Collection<?> collection) {
            return collection.toArray(new Object[0]);
        } else {
            return new Object[] { value };
        }
    }
}
