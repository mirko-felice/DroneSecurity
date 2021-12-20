package shipping.courier;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import shipping.courier.entities.PlacedOrder;

import java.util.Objects;

/**
 * Represents the Service to perform operations useful to the Courier.
 */
public final class CourierShippingService {

    private static final String OPEN_API_FILENAME = "courierShippingService.json";
    private static final String PERFORM_DELIVERY_OPERATION_ID = "performDelivery";
    private static final String RESCHEDULE_DELIVERY_OPERATION_ID = "rescheduleDelivery";
    private static final String CORRECT_RESPONSE_TO_PERFORM_DELIVERY = "Delivery is performing...";
    private static final String CORRECT_RESPONSE_TO_RESCHEDULE_DELIVERY = "Order rescheduled.";
    private static final int PORT = 80;
    private final transient Vertx vertx;

    /**
     * Build the Service.
     */
    public CourierShippingService() {
        this.vertx = Vertx.vertx();
    }

    /**
     * Start listening to HTTP methods.
     */
    public void startListening() {
        RouterBuilder.create(this.vertx, Objects.requireNonNull(getClass().getResource(OPEN_API_FILENAME)).toString())
                .onSuccess(routerBuilder -> {
                    this.setupOperations(routerBuilder);
                    this.vertx.createHttpServer().requestHandler(routerBuilder.createRouter()).listen(PORT);
                })
                .onFailure(Throwable::printStackTrace);
    }

    private void setupOperations(final RouterBuilder routerBuilder) {
        routerBuilder.operation(PERFORM_DELIVERY_OPERATION_ID)
                .handler(this::setupPerformDelivery);
        routerBuilder.operation(RESCHEDULE_DELIVERY_OPERATION_ID)
                .handler(this::setupRescheduleDelivery);
    }

    private void setupPerformDelivery(final RoutingContext routingContext) {
        RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        PlacedOrder order = params.body().getJsonObject().mapTo(PlacedOrder.class);
        System.out.println(order); // TODO check body
        var deliveringOrder = order.deliver();
        System.out.println(deliveringOrder.getCurrentState());
        routingContext.response().end(CORRECT_RESPONSE_TO_PERFORM_DELIVERY);
    }

    private void setupRescheduleDelivery(final RoutingContext routingContext) {
        routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        // TODO refactor because needed NEW Date and order ID
        routingContext.response().end(CORRECT_RESPONSE_TO_RESCHEDULE_DELIVERY);
    }

}
