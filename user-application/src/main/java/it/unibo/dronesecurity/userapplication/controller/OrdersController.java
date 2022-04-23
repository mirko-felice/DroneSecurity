package it.unibo.dronesecurity.userapplication.controller;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import it.unibo.dronesecurity.lib.AlertUtils;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.Order;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.PlacedOrder;
import it.unibo.dronesecurity.userapplication.utilities.ClientHelper;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;
import it.unibo.dronesecurity.userapplication.utilities.UserHelper;
import it.unibo.dronesecurity.userapplication.utilities.OrderConstants;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller dedicated to starting application.
 */
public final class OrdersController implements Initializable {

    private static final String HOST = "http://localhost:";
    private static final int PORT = 80;
    private static final String BASE_URI = HOST + PORT + "/courierShippingService";
    private static final String LIST_ORDERS_URI = BASE_URI + "/listOrders";
    private static final String PERFORM_DELIVERY_URI = BASE_URI + "/performDelivery";
    private static final String RESCHEDULE_DELIVERY_URI = BASE_URI + "/rescheduleDelivery";
    private static final String MONITORING_FILENAME = "monitoring.fxml";
    private static final Runnable NOT_SELECTED_RUNNABLE = () ->
            AlertUtils.showWarningAlert("You MUST first select an order.");
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, String> productColumn;
    @FXML private TableColumn<Order, String> stateColumn;
    @FXML private Button performDeliveryButton;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.orderDateColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                DateHelper.FORMATTER.format(cell.getValue().getPlacingDate())));
        this.productColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getProduct()));
        this.stateColumn.setCellValueFactory(new PropertyValueFactory<>("currentState"));
        ClientHelper.WEB_CLIENT.get(LIST_ORDERS_URI)
                .send(r -> {
                    if (r.succeeded()) {
                        final List<Order> orders = r.result().bodyAsJsonArray().stream()
                                .map(o -> Json.decodeValue(o.toString(), Order.class))
                                .collect(Collectors.toList());
                        this.table.setItems(FXCollections.observableList(orders));
                    }
        });
    }

    @FXML
    private void performDelivery() {
        final Optional<Order> selectedOrder = this.getSelectedOrder();
        selectedOrder.ifPresentOrElse(order -> {
            // TODO how to check ???
            if (order instanceof PlacedOrder) {
                final JsonObject body = new JsonObject()
                        .put(OrderConstants.ORDER_KEY, order)
                        .put(OrderConstants.COURIER_KEY, UserHelper.getLoggedUser());
                ClientHelper.WEB_CLIENT.post(PERFORM_DELIVERY_URI)
                        .putHeader("Content-Type", "application/json")
                        .sendBuffer(body.toBuffer())
                        .onSuccess(h -> Platform.runLater(() -> {
                            try {
                                ((Stage) this.performDeliveryButton.getScene().getWindow()).close();
                                final URL fileUrl = getClass().getResource(MONITORING_FILENAME);
                                final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
                                fxmlLoader.setController(new MonitorController());
                                final Scene scene = new Scene(fxmlLoader.load());
                                final Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.setTitle("Monitoring...");
                                stage.setOnCloseRequest(event -> {
                                    Connection.getInstance().closeConnection();
                                    ClientHelper.WEB_CLIENT.close();
                                    Platform.exit();
                                    System.exit(0);
                                });
                                stage.show();
                            } catch (IOException e) {
                                LoggerFactory.getLogger(getClass()).error("Error creating the new window:", e);
                            }
                        }));
            } else
                AlertUtils.showErrorAlert("You can NOT deliver an order that isn't placed.");
        }, NOT_SELECTED_RUNNABLE);
    }

    @FXML
    private void rescheduleDelivery() {
        final Optional<Order> selectedOrder = this.getSelectedOrder();
        // TODO think about checking if getCurrentState().contains("fail") or instanceof FailedOrder
        selectedOrder.ifPresentOrElse(order -> {
            if (order.getCurrentState().contains("fail"))
                ClientHelper.WEB_CLIENT.post(RESCHEDULE_DELIVERY_URI)
                        .putHeader("Content-Type", "application/json")
                        .sendBuffer(Json.encodeToBuffer(order));
            else
                AlertUtils.showErrorAlert("You can NOT reschedule an order that isn't failed.");
            }, NOT_SELECTED_RUNNABLE);
    }

    private Optional<Order> getSelectedOrder() {
        return Optional.ofNullable(this.table.getSelectionModel().getSelectedItem());
    }
}
