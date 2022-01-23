package it.unibo.dronesecurity.userapplication.shipping.courier.specifications;

import it.unibo.dronesecurity.userapplication.shipping.courier.entities.Order;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.OrderSnapshot;

/**
 * Specification to check that {@link Order} must NOT have null or empty product!
 */
public final class NoEmptyProduct implements Specification<OrderSnapshot> {

    private static final String EMPTY_PRODUCT = "";

    @Override
    public boolean isSatisfiedBy(final OrderSnapshot entity) {
        // TODO maybe refactor using: @NotNull on entity product
        return entity.getProduct() != null && !entity.getProduct().equals(EMPTY_PRODUCT);
    }
}
