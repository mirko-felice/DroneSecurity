package shipping.courier;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import org.jetbrains.annotations.NotNull;
import shipping.courier.entities.DeliveringOrder;
import shipping.courier.entities.Order;
import shipping.courier.entities.PlacedOrder;
import shipping.courier.repo.OrderRepository;
import utilities.CastHelper;
import utilities.CustomLogger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the Service to perform operations useful to the Courier.
 */
public final class CourierShippingService {

    private static final String OPEN_API_FILENAME = "courierShippingService.json";
    private static final String PERFORM_DELIVERY_OPERATION_ID = "performDelivery";
    private static final String RESCHEDULE_DELIVERY_OPERATION_ID = "rescheduleDelivery";
    private static final String GET_ORDERS_OPERATION_ID = "getOrders";
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

    private void setupOperations(final @NotNull RouterBuilder routerBuilder) {
        routerBuilder.operation(PERFORM_DELIVERY_OPERATION_ID)
                .handler(this::setupPerformDelivery);
        routerBuilder.operation(RESCHEDULE_DELIVERY_OPERATION_ID)
                .handler(this::setupRescheduleDelivery);
        routerBuilder.operation(GET_ORDERS_OPERATION_ID)
                .handler(this::setupGetOrders);
    }

    private void setupPerformDelivery(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final Optional<PlacedOrder> optionalOrder =
                CastHelper.safeCast(params.body().getJsonObject().mapTo(Order.class), PlacedOrder.class);
        if (optionalOrder.isPresent()) {
            final PlacedOrder order = optionalOrder.get();
            CustomLogger.getLogger(getClass().getName()).info(order.toString()); // TODO check body
            final DeliveringOrder deliveringOrder = order.deliver();
            OrderRepository.getInstance().delivering(deliveringOrder);
            CustomLogger.getLogger(getClass().getName()).info(deliveringOrder.getCurrentState());
            routingContext.response().end(CORRECT_RESPONSE_TO_PERFORM_DELIVERY);
        }
    }

    private void setupRescheduleDelivery(final @NotNull RoutingContext routingContext) {
        routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        // TODO refactor because needed NEW Date and order ID
        routingContext.response().end(CORRECT_RESPONSE_TO_RESCHEDULE_DELIVERY);
    }

    private void setupGetOrders(final @NotNull RoutingContext routingContext) {
        final List<Order> orders = OrderRepository.getInstance().getOrders();
        routingContext.response()
                .putHeader("Content-Type", "application/json")
                .send(Json.encodePrettily(orders));
    }
}
