package it.unibo.dronesecurity.userapplication.controller;

import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import it.unibo.dronesecurity.lib.CustomLogger;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.Order;
import it.unibo.dronesecurity.userapplication.shipping.courier.entities.PlacedOrder;
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
import it.unibo.dronesecurity.userapplication.utilities.AlertUtils;
import it.unibo.dronesecurity.userapplication.utilities.DateHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller dedicated to starting application.
 */
public final class StartController implements Initializable {

    private static final String HOST = "http://localhost";
    private static final int PORT =  8080;
    private static final String BASE_URI = HOST + PORT;
    private static final String GET_ORDERS_URI = BASE_URI + "/getOrders";
    private static final String PERFORM_DELIVERY_URI = BASE_URI + "/performDelivery";
    private static final String RESCHEDULE_DELIVERY_URI = BASE_URI + "/rescheduleDelivery";
    private static final String MONITORING_FILENAME = "monitoring.fxml";
    private static final Runnable NOT_SELECTED_RUNNABLE = () ->
            AlertUtils.showWarningAlert("You MUST first select an order.");
    private final transient WebClient client;
    @FXML private transient TableView<Order> table;
    @FXML private transient TableColumn<Order, String> orderDateColumn;
    @FXML private transient TableColumn<Order, String> productColumn;
    @FXML private transient TableColumn<Order, String> stateColumn;
    @FXML private transient Button performDeliveryButton;

    /**
     * Build the Controller using the client to interact with services.
     * @param client client to use to interact with services
     */
    public StartController(final WebClient client) {
        this.client = client;
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.orderDateColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(DateHelper.FORMATTER.format(cell.getValue().getSnapshot().getOrderDate())));
        this.productColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getSnapshot().getProduct()));
        this.stateColumn.setCellValueFactory(new PropertyValueFactory<>("currentState"));
        this.client.get(GET_ORDERS_URI)
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
            if (order instanceof PlacedOrder)
                this.client.post(PERFORM_DELIVERY_URI)
                        .putHeader("Content-Type", "application/json")
                        .sendBuffer(Json.encodeToBuffer(order))
                        .onComplete(v -> CustomLogger.getLogger(getClass().getName()).info(v.result().bodyAsString()))
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
                                    this.client.close();
                                    Platform.exit();
                                    System.exit(0);
                                });
                                stage.show();
                            } catch (IOException e) {
                                CustomLogger.getLogger(getClass().getName())
                                        .severe("Error creating the new window:", e);
                            }
                        }));
            else
                AlertUtils.showErrorAlert("You can NOT deliver an order that isn't placed.");
        }, NOT_SELECTED_RUNNABLE);
    }

    @FXML
    private void rescheduleDelivery() {
        final Optional<Order> selectedOrder = this.getSelectedOrder();
        // TODO think about checking if getCurrentState().contains("fail") or instanceof FailedOrder
        selectedOrder.ifPresentOrElse(order -> {
            if (order.getCurrentState().contains("fail"))
                this.client.post(RESCHEDULE_DELIVERY_URI)
                        .putHeader("Content-Type", "application/json")
                        .sendBuffer(Json.encodeToBuffer(order))
                        .onComplete(v -> CustomLogger.getLogger(getClass().getName()).info(v.result().bodyAsString()));
            else
                AlertUtils.showErrorAlert("You can NOT reschedule an order that isn't failed.");
            }, NOT_SELECTED_RUNNABLE);
    }

    private Optional<Order> getSelectedOrder() {
        return Optional.ofNullable(this.table.getSelectionModel().getSelectedItem());
    }
}
