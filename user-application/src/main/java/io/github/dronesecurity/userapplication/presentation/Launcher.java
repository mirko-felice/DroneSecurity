/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation;

import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.PropertiesConstants;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.infrastructure.shipping.repo.MongoOrderRepository;
import io.github.dronesecurity.userapplication.presentation.shipping.DroneAPI;
import io.github.dronesecurity.userapplication.presentation.shipping.ShippingAPI;
import io.github.dronesecurity.userapplication.presentation.user.UserAPI;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.github.dronesecurity.userapplication.utilities.shipping.ShippingAPIHelper;
import io.vertx.core.json.JsonObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.security.SecureRandom;

/**
 * Launch the application.
 */
public final class Launcher extends Application {

    private static final int INITIAL_SIZE = 10;
    private static final String[] FAKE_PRODUCTS = {
            "HDD", "SSD", "MOUSE", "KEYBOARD", "HEADSET", "MONITOR", "WEBCAM", "CONTROLLER", "USB", "HDMI" };
    private static final String[] FAKE_CLIENTS = {
            "John", "James", "Robert", "Mary", "Jennifer", "Patricia", "David", "William", "Micheal", "Anthony" };
    private static final SecureRandom RANDOM_GENERATOR = new SecureRandom();

    private static final double CONNECTION_MIN_WIDTH = 400;
    private static final double CONNECTION_MIN_HEIGHT = 600;
    private static final double LOGIN_MIN_WIDTH = 350;
    private static final double LOGIN_MIN_HEIGHT = 200;
    private static final String CONNECTION_FXML = "connection.fxml";
    private static final String LOGIN_FXML = "login.fxml";

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final @NotNull Stage stage) {
        final File propertiesFile = new File(PropertiesConstants.PROPERTIES_FILENAME);
        if (!propertiesFile.exists() || DialogUtils.showConfirmationDialog("File properties already found!",
                "Would you like to reset values?")) {
            final FXMLLoader fxmlLoader = new FXMLLoader(ConnectionController.class.getResource(CONNECTION_FXML));
            FXHelper.initializeWindow(stage, "Connection settings", fxmlLoader, CONNECTION_MIN_WIDTH,
                    CONNECTION_MIN_HEIGHT).ifPresent(window -> {
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
        VertxHelper.VERTX.deployVerticle(ShippingAPI.class.getName()).onSuccess(res ->
                new Thread(() -> {
                    if (new MongoOrderRepository().listOrders().isEmpty()) {
                        for (int i = 1; i <= INITIAL_SIZE; i++) {
                            final String productName = FAKE_PRODUCTS[RANDOM_GENERATOR.nextInt(INITIAL_SIZE)];
                            final String clientName = FAKE_CLIENTS[RANDOM_GENERATOR.nextInt(INITIAL_SIZE)];
                            ShippingAPIHelper.postJson(ShippingAPIHelper.Operation.PLACE_ORDER,
                                    createBody(productName, clientName));
                        }
                    }
                }).start());
        VertxHelper.VERTX.deployVerticle(UserAPI.class.getName());
        VertxHelper.VERTX.deployVerticle(DroneAPI.class.getName());
        launch(args);
    }

    private static @NotNull JsonObject createBody(final String productName, final String clientName) {
        final JsonObject body = new JsonObject();
        body.put(ShippingAPIHelper.CLIENT_NAME_KEY, clientName);
        body.put(ShippingAPIHelper.PRODUCT_NAME_KEY, productName);
        body.put(ShippingAPIHelper.ESTIMATED_ARRIVAL_KEY, OrderDate.TOMORROW.asString());
        return body;
    }

    private void showLogin() {
        Connection.getInstance().connect();
        Platform.runLater(() -> {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
            FXHelper.initializeWindow(Modality.NONE, "Login", fxmlLoader, LOGIN_MIN_WIDTH, LOGIN_MIN_HEIGHT)
                    .ifPresent(Stage::show);
        });
    }
}
