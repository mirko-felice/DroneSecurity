package it.unibo.dronesecurity.userapplication.controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import it.unibo.dronesecurity.userapplication.shipping.courier.CourierShippingService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * Launch the application.
 */
public final class Launcher extends Application {

    private static final String LOGIN_FXML = "login.fxml";
    private final transient WebClient client = WebClient.create(Vertx.vertx());

    @Override
    public void start(final @NotNull Stage stage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
        fxmlLoader.setController(new AuthenticationController(this.client));
        final Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            this.client.close();
            Platform.exit();
            System.exit(0);
        });
        stage.setTitle("Simple Title");
        stage.show();
    }

    /**
     * Main method.
     * @param args additional arguments
     */
    public static void main(final String[] args) {
        new CourierShippingService().startListening();
        launch(args);
    }
}
