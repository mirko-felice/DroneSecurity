package it.unibo.dronesecurity.dronesystem.drone;

import it.unibo.dronesecurity.lib.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Application that activates Drone Service.
 */
public final class Activation extends Application {

    private static final String CONNECTION_FXML = "connection.fxml";
    private static final String FILE_PROPERTIES_NOT_FOUND =
            "File .properties not found! Generate file adding 'generate-properties' as parameter to execute command";

    @Override
    public void start(final Stage stage) throws IOException {
        final File propertiesFile = new File(PropertiesConstants.PROPERTIES_FILENAME);

        if (getParameters().getRaw().contains("generate-properties")) {
            if (!propertiesFile.exists() || AlertUtils.showConfirmationAlert("File properties already found!",
                    "Would you like to reset values?")) {
                final FXMLLoader fxmlLoader = new FXMLLoader(ConnectionController.class.getResource(CONNECTION_FXML));
                fxmlLoader.setController(new ConnectionController());
                final Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.setOnCloseRequest(event -> {
                    Platform.exit();
                    System.exit(0);
                });
                stage.setTitle("Connection settings");
                stage.setOnHidden(ignored -> this.startDroneService());
                stage.show();
            } else {
                this.connectAndStart();
            }
        } else {
            if (propertiesFile.exists()) {
                this.connectAndStart();
            } else {
                Platform.exit();
                LoggerFactory.getLogger(getClass()).error(FILE_PROPERTIES_NOT_FOUND);
            }
        }
    }

    /**
     * Main method for drone activation.
     *
     * @param args the args that are passed to the application
     */
    public static void main(final String[] args) {
        launch(args);
    }

    private void connectAndStart() {
        Connection.getInstance().connect();
        this.startDroneService();
    }

    private void startDroneService() {
        Platform.exit();
        new DroneService().waitForDeliveryAssignment();
    }
}
