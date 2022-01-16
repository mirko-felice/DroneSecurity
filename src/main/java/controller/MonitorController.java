package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import utilities.CustomLogger;

/**
 * Controller dedicated to monitoring delivery.
 */
public class MonitorController {

    @FXML private transient Button confirmDeliveryButton;
    @FXML private transient Button failDeliveryButton;

    @FXML
    private void failDelivery() {
        CustomLogger.getLogger(getClass().getName()).info("Logic will be implemented.");
    }

    @FXML
    private void confirmDelivery() {
        CustomLogger.getLogger(getClass().getName()).info("Logic will be implemented.");
    }
}
