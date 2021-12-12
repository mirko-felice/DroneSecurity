package shipping.courier;

/**
 * Specification applicable to whatever entity.
 * @param <T> the type of the entity that should satisfy the specification
 */
public interface Specification<T> {

    /**
     * Checks if the entity satisfies the specification.
     * @param entity the entity that should satisfy the specification
     * @return true if {@code entity} satisfy specification
     */
    boolean isSatisfiedBy(T entity);
}
