package it.unibo.dronesecurity.userapplication.controller;

import it.unibo.dronesecurity.lib.AlertUtils;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.ConnectionController;
import it.unibo.dronesecurity.lib.PropertiesConstants;
import it.unibo.dronesecurity.userapplication.utilities.FXHelper;
import it.unibo.dronesecurity.userapplication.utilities.VertxHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Launch the application.
 */
public final class Launcher extends Application {

    private static final String CONNECTION_FXML = "connection.fxml";
    private static final String LOGIN_FXML = "login.fxml";

    @Override
    public void start(final @NotNull Stage stage) {
        final File propertiesFile = new File(PropertiesConstants.PROPERTIES_FILENAME);
        if (!propertiesFile.exists() || AlertUtils.showConfirmationAlert("File properties already found!",
                "Would you like to reset values?")) {
            final FXMLLoader fxmlLoader = new FXMLLoader(ConnectionController.class.getResource(CONNECTION_FXML));
            FXHelper.initializeWindow(stage, "Connection settings", fxmlLoader).ifPresent(window -> {
                window.setOnCloseRequest(unused -> {
                    VertxHelper.WEB_CLIENT.close();
                    VertxHelper.MONGO_CLIENT.close();
                    VertxHelper.VERTX.close();
                    Platform.exit();
                    System.exit(0);
                });
                window.setOnHidden(ignored -> this.showLogin());
                window.show();
            });
        } else
            this.showLogin();
    }

    /**
     * Main method.
     * @param args additional arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }

    private void showLogin() {
        Connection.getInstance().connect();
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
        FXHelper.initializeWindow(Modality.NONE, "Login", fxmlLoader).ifPresent(Stage::show);
    }
}
