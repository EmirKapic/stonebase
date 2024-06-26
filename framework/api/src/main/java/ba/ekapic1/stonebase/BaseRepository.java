package ba.ekapic1.stonebase;

import ba.ekapic1.stonebase.filter.FilterBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The base repository which all other repositories should extend.
 * @param <M> the type of JPA entity
 * @param <ID> the type of said entities ID column
 */
public interface BaseRepository<M, ID> extends JpaRepository<M, ID>, JpaSpecificationExecutor<M> {
    /**
     * Returns a fresh instance of a filter builder.
     * <p>
     * This instance remains until you call the <code>build</code> method and all method calls on it <b>will mutate</b> the instance.
     * This means the instances are not to be reused over multiple calls, and you should instead get another instance of the builder for every query.
     * </p>
     */
    default FilterBuilder<M> filterBuilder() {
        return FilterBuilder.simple();
    }

    /**
     * <p>
     * Convenience method to be used when we are sure our entity will be found in the database.
     * Will throw exceptions if said entity is not found.
     * </p>
     * If unsure of existence, use {@link JpaRepository}'s <code>findOne</code> method which is also exposed through this repository.
     */
    default M get(final ID modelId) {
        return findById(modelId).orElseThrow();
    }

    /**
     * <p>
     * Convenience method to be used when we are sure our entity will be found in the database.
     * Will throw exceptions if said entity is not found.
     * </p>
     * If unsure of existence, use {@link JpaRepository}'s <code>findOne</code> method which is also exposed through this repository.
     */
    default M get(final Specification<M> specification) {
        return findOne(specification).orElseThrow();
    }
}
