package controller;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import shipping.courier.entities.AbstractOrder;
import shipping.courier.entities.Order;
import utilities.DateHelper;

import java.net.URL;
import java.util.List;
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
    private final transient WebClient client = WebClient.create(Vertx.vertx());
    @FXML private transient TableView<Order> table;
    @FXML private transient TableColumn<Order, String> orderDateColumn;
    @FXML private transient TableColumn<Order, String> productColumn;
    @FXML private transient TableColumn<Order, String> stateColumn;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.orderDateColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(DateHelper.FORMATTER.format(cell.getValue().getSnapshot().getOrderDate())));
        this.productColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getSnapshot().getProduct()));
        this.stateColumn.setCellValueFactory(new PropertyValueFactory<>("currentState"));
        this.client.get(GET_ORDERS_URI)
                .send()
                .onComplete(r -> {
                    if (r.succeeded()) {
                        final List<Order> orders = r.result().bodyAsJsonArray().stream()
                                .map(o -> Json.decodeValue(o.toString(), AbstractOrder.class))
                                .collect(Collectors.toList());
                        this.table.setItems(FXCollections.observableList(orders));
                    }
        });
    }
}
