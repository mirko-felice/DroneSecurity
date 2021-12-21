package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import shipping.courier.CourierShippingService;

import java.util.Objects;

public class Controller extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        TitledPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("app.fxml")));
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    public static void main(final String[] args) {
        new CourierShippingService().startListening();
        launch(args);
    }
}
