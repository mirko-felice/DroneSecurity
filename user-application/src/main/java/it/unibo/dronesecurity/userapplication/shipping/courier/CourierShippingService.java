package it.unibo.dronesecurity.userapplication.shipping.courier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.lib.MqttMessageValueConstants;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.DeliveringOrder;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.PlacedOrder;
import it.unibo.dronesecurity.userapplication.shipping.courier.repo.OrderRepository;
import it.unibo.dronesecurity.userapplication.utilities.CastHelper;
import it.unibo.dronesecurity.userapplication.utilities.OrderConstants;
import org.jetbrains.annotations.NotNull;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the Service to perform operations useful to the Courier.
 */
public final class CourierShippingService extends AbstractVerticle {

    private static final String OPEN_API_URL = "https://raw.githubusercontent.com/mirko-felice/DroneSecurity/develop/"
            + "user-application/src/main/resources/it/unibo/dronesecurity/userapplication/shipping/courier/"
            + "courierShippingService.json";
    private static final String PERFORM_DELIVERY_OPERATION_ID = "performDelivery";
    private static final String RESCHEDULE_DELIVERY_OPERATION_ID = "rescheduleDelivery";
    private static final String LIST_ORDERS_OPERATION_ID = "listOrders";
    private static final String CORRECT_RESPONSE_TO_PERFORM_DELIVERY = "Delivery is performing...";
    private static final String CORRECT_RESPONSE_TO_RESCHEDULE_DELIVERY = "Order rescheduled.";
    private static final String DEFAULT_KEY = "default";
    private static final String SEP = "/";
    private static final int CLIENT_ERROR_CODE = 400;

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
        super.start(startPromise);
        final Router globalRouter = Router.router(this.getVertx());
        RouterBuilder.create(this.getVertx(), OPEN_API_URL)
                .onSuccess(routerBuilder -> {
                    this.setupOperations(routerBuilder);

                    final JsonArray servers = routerBuilder.getOpenAPI().getOpenAPI().getJsonArray("servers");
                    final int serversCount = servers.size();
                    final List<Future<?>> futures = new ArrayList<>(serversCount);
                    for (int i = 0; i < serversCount; i++) {
                        final JsonObject server = servers.getJsonObject(i);
                        final JsonObject variables = server.getJsonObject("variables");

                        final String basePath = SEP + variables.getJsonObject("basePath").getString(DEFAULT_KEY);
                        final int port = Integer.parseInt(variables.getJsonObject("port").getString(DEFAULT_KEY));
                        final String host = variables.getJsonObject("host").getString(DEFAULT_KEY);

                        globalRouter.mountSubRouter(basePath, routerBuilder.createRouter());
                        futures.add(this.getVertx().createHttpServer().requestHandler(globalRouter).listen(port, host));
                    }
                    CompositeFuture.all(Arrays.asList(futures.toArray(new Future[0])))
                            .onSuccess(ignored -> startPromise.complete());
                })
                .onFailure(startPromise::fail);
    }

    private void setupOperations(final @NotNull RouterBuilder routerBuilder) {
        routerBuilder.operation(PERFORM_DELIVERY_OPERATION_ID)
                .handler(this::performDelivery);
        routerBuilder.operation(RESCHEDULE_DELIVERY_OPERATION_ID)
                .handler(this::rescheduleDelivery);
        routerBuilder.operation(LIST_ORDERS_OPERATION_ID)
                .handler(this::listOrders);
    }

    private void performDelivery(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final PlacedOrder order = body.getJsonObject(OrderConstants.ORDER_KEY).mapTo(PlacedOrder.class);
        if (order == null)
            routingContext.response().setStatusCode(CLIENT_ERROR_CODE).end();
        else {
            final DeliveringOrder deliveringOrder = order.deliver();
            OrderRepository.getInstance().delivering(deliveringOrder);

            final JsonNode messageJson = new ObjectMapper().createObjectNode()
                    .put(MqttMessageParameterConstants.SYNC_PARAMETER,
                            MqttMessageValueConstants.PERFORM_DELIVERY_MESSAGE)
                    .put(MqttMessageParameterConstants.ORDER_ID_PARAMETER, order.getId())
                    .put(MqttMessageParameterConstants.COURIER_PARAMETER, body.getString(OrderConstants.COURIER_KEY));
            final Connection connection = Connection.getInstance();
            connection.publish(MqttTopicConstants.ORDER_TOPIC, messageJson);

            connection.subscribe(MqttTopicConstants.LIFECYCLE_TOPIC, this::callback);
            routingContext.response().end(CORRECT_RESPONSE_TO_PERFORM_DELIVERY);
        }
    }

    private void callback(final @NotNull MqttMessage msg) {
        final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
        final String orderId = json.getString(MqttMessageParameterConstants.ORDER_ID_PARAMETER);
        final String statusValue = json.getString(MqttMessageParameterConstants.STATUS_PARAMETER);
        final OrderRepository orderRepository = OrderRepository.getInstance();
        CastHelper.safeCast(orderRepository.getOrderById(orderId), DeliveringOrder.class).ifPresent(order -> {
            final Connection connection = Connection.getInstance();
            if (MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE.equals(statusValue))
                orderRepository.confirmedDelivery(order.confirmDelivery());
            else if (MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE.equals(statusValue))
                orderRepository.failedDelivery(order.failDelivery());

            // TODO maybe catch else branch that does NOT send this message
            final JsonNode message = new ObjectMapper().createObjectNode()
                    .put(MqttMessageParameterConstants.SYNC_PARAMETER,
                            MqttMessageValueConstants.DRONE_CALLBACK_MESSAGE);
            connection.publish(MqttTopicConstants.ORDER_TOPIC, message);
        });
    }

    private void rescheduleDelivery(final @NotNull RoutingContext routingContext) {
        routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        // TODO refactor because needed NEW Date and order ID
        routingContext.response().end(CORRECT_RESPONSE_TO_RESCHEDULE_DELIVERY);
    }

    private void listOrders(final @NotNull RoutingContext routingContext) {
        OrderRepository.getInstance().getOrders().onSuccess(orders -> routingContext.response()
                .putHeader("Content-Type", "application/json")
                .send(Json.encodePrettily(orders)));
    }
}
