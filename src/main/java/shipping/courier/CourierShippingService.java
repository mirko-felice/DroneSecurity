package shipping.courier;

import io.vertx.core.Vertx;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.util.Objects;

/**
 * Represents the Service to perform operations useful to the Courier.
 */
public final class CourierShippingService {

    private static final String OPEN_API_FILENAME = "courierShippingService.json";
    private static final String PERFORM_DELIVERY_OPERATION_ID = "performDelivery";
    private static final String CORRECT_RESPONSE_TO_PERFORM_DELIVERY = "Delivery is performing...";
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
                .handler(routingContext -> {
                    RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
                    System.out.println(params.body().getJsonObject()); // TODO check body
                    routingContext.response().end(CORRECT_RESPONSE_TO_PERFORM_DELIVERY);
                });
    }
}
