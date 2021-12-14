package shipping.courier.entities;

/**
 * Specification to check that {@link Order} must NOT have null or empty product!
 */
public final class NoEmptyProduct implements Specification<OrderSnapshot> {

    private static final String EMPTY_PRODUCT = "";

    @Override
    public boolean isSatisfiedBy(final OrderSnapshot entity) {
        return entity.getProduct() != null && !entity.getProduct().equals(EMPTY_PRODUCT);
    }
}
