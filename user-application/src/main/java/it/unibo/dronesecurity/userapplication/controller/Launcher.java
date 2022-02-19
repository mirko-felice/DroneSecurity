package it.unibo.dronesecurity.userapplication.controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import it.unibo.dronesecurity.lib.*;
import it.unibo.dronesecurity.userapplication.shipping.courier.CourierShippingService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Launch the application.
 */
public final class Launcher extends Application {

    private static final String CONNECTION_FXML = "connection.fxml";
    private static final String LOGIN_FXML = "login.fxml";

    @Override
    public void start(final @NotNull Stage stage) throws Exception {
        final File propertiesFile = new File(PropertiesConstants.PROPERTIES_FILENAME);
        if (!propertiesFile.exists() || AlertUtils.showConfirmationAlert("File properties already found!",
                "Would you like to reset values?")) {
            stage.setResizable(false);
            final FXMLLoader fxmlLoader = new FXMLLoader(ConnectionController.class.getResource(CONNECTION_FXML));
            fxmlLoader.setController(new ConnectionController());
            final Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setTitle("Connection settings");
            stage.setOnHidden(ignored -> this.showLogin());
            stage.show();
        } else
            this.showLogin();
    }

    /**
     * Main method.
     * @param args additional arguments
     */
    public static void main(final String[] args) {
        new CourierShippingService().startListening();
        launch(args);
    }

    private void showLogin() {
        try {
            Connection.getInstance().connect();
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
            final WebClient client = WebClient.create(Vertx.vertx());
            fxmlLoader.setController(new AuthenticationController());
            final Stage stage = new Stage();
            final Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                client.close();
                Platform.exit();
                System.exit(0);
            });
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Can NOT load login interface.", e);
        }
    }
}
