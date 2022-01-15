package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import shipping.courier.CourierShippingService;

import java.util.Objects;

/**
 * Launch the application.
 */
public final class Launcher extends Application {

    private static final String STARTING_FILENAME = "start.fxml";

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final TitledPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(STARTING_FILENAME)));
        final Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
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
