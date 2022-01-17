package controller;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import shipping.courier.CourierShippingService;

/**
 * Launch the application.
 */
public final class Launcher extends Application {

    private static final String STARTING_FILENAME = "start.fxml";
    private final transient WebClient client = WebClient.create(Vertx.vertx());

    @Override
    public void start(final @NotNull Stage primaryStage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(STARTING_FILENAME));
        fxmlLoader.setController(new StartController(this.client));
        final TitledPane pane = fxmlLoader.load();
        final Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            this.client.close();
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setTitle("Simple Title");
        primaryStage.show();
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
