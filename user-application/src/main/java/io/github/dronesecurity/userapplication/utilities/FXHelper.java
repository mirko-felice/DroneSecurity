/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities;

import io.github.dronesecurity.lib.connection.Connection;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Helper providing methods related to JavaFX components.
 */
public final class FXHelper {

    private FXHelper() { }

    /**
     * Initialize a stage using parameters.
     * @param stage stage to initialize
     * @param title title to set on the window
     * @param loader loader to use to load the scene from fxml
     * @param minWidth minimum width of the window
     * @param minHeight minimum height of the window
     * @return an {@link Optional} indicating if an error happened during the loading of resource file or not
     */
    public static Optional<Stage> initializeWindow(final @NotNull Stage stage,
                                                   final String title,
                                                   final @NotNull FXMLLoader loader,
                                                   final double minWidth,
                                                   final double minHeight) {
        try {
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
            stage.setMinWidth(minWidth);
            stage.setMinHeight(minHeight);
            if (stage.getModality() == Modality.NONE)
                stage.setOnCloseRequest(unused -> {
                    VertxHelper.WEB_CLIENT.close();
                    VertxHelper.MONGO_CLIENT.close();
                    VertxHelper.VERTX.close();
                    Connection.getInstance().closeConnection();
                    Platform.exit();
                    System.exit(0);
                });
            return Optional.of(stage);
        } catch (IOException e) {
            LoggerFactory.getLogger(FXHelper.class).error("Error creating the new window:", e);
            return Optional.empty();
        }
    }

    /**
     * Creates basic window using parameters.
     * @param modality modality to initialize window
     * @param title title to set on the window
     * @param loader loader to use to load the scene from fxml
     * @param minWidth minimum width of the window
     * @param minHeight minimum height of the window
     * @return an {@link Optional} indicating if an error happened during the loading of resource file or not
     */
    public static Optional<Stage> initializeWindow(final Modality modality,
                                                   final String title,
                                                   final @NotNull FXMLLoader loader,
                                                   final double minWidth,
                                                   final double minHeight) {
        final Stage stage = new Stage();
        stage.initModality(modality);
        return initializeWindow(stage, title, loader, minWidth, minHeight);
    }

    /**
     * Cell showing proximity value.
     * @param <T> type parameter of the table using this cell
     */
    public static class ProximityCell<T> extends TextFieldTableCell<T, Double> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void updateItem(final Double value, final boolean empty) {
            super.updateItem(value, empty);
            if (!empty)
                setText(value + " cm");
        }
    }

    /**
     * Cell showing one of the accelerometer angle.
     * @param <T> type parameter of the table using this cell
     */
    public static class AngleCell<T> extends TextFieldTableCell<T, Integer> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void updateItem(final Integer value, final boolean empty) {
            super.updateItem(value, empty);
            if (!empty && value == null)
                setText("Not available.");
            else if (value != null)
                setText(value + " \u00B0");
        }
    }

    /**
     * Cell showing camera value.
     * @param <T> type parameter of the table using this cell
     */
    public static class CameraCell<T> extends TextFieldTableCell<T, Long> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void updateItem(final Long value, final boolean empty) {
            super.updateItem(value, empty);
            if (!empty)
                setText(value + " bytes");
        }
    }
}
