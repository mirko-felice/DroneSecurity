package controller;

import io.vertx.ext.web.client.WebClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import utilities.CustomLogger;

/**
 * Controller dedicated to monitoring delivery.
 */
public class MonitorController {

    @FXML private transient Button confirmDeliveryButton;
    @FXML private transient Button failDeliveryButton;

    /**
     * Build the Controller using the client to interact with services.
     * @param client client to use to interact with services
     */
    public MonitorController(final WebClient client) {
        CustomLogger.getLogger(getClass().getName()).info(client.toString());
    }

    @FXML
    private void failDelivery() {
        CustomLogger.getLogger(getClass().getName()).info("Logic will be implemented.");
    }

    @FXML
    private void confirmDelivery() {
        CustomLogger.getLogger(getClass().getName()).info("Logic will be implemented.");
    }
}
