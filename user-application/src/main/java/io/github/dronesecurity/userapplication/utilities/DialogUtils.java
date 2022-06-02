/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Window;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

/**
 * Provider of {@link Alert} utility methods.
 */
public final class DialogUtils {

    private DialogUtils() { }

    /**
     * Shows an Information {@link Alert.AlertType} {@link Alert} without header.
     * @param contentMessage the message to show
     * @param runnable {@link Runnable} to run on hidden event
     */
    public static void showInfoDialog(final String contentMessage, final Runnable runnable) {
        Platform.runLater(() -> {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText(null);
            alert.setContentText(contentMessage);
            alert.setOnHidden(ignored -> runnable.run());
            alert.showAndWait();
        });
    }

    /**
     * Shows a Warning {@link Alert.AlertType} {@link Alert} without header.
     * @param contentMessage the message to show
     */
    public static void showWarningDialog(final String contentMessage) {
        Platform.runLater(() -> {
            final Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText(contentMessage);
            alert.showAndWait();
        });
    }

    /**
     * Shows an Error {@link Alert.AlertType} {@link Alert} without header.
     * @param contentMessage the message to show
     */
    public static void showErrorDialog(final String contentMessage) {
        Platform.runLater(() -> {
            final Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText(contentMessage);
            alert.showAndWait();
        });
    }

    /**
     * Shows a Confirmation {@link Alert.AlertType} {@link Alert} without header.
     * @param title title of the alert
     * @param contentMessage the message to show
     * @return true if confirmation happens
     */
    public static boolean showConfirmationDialog(final String title, final String contentMessage) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contentMessage, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
        return alert.getResult().equals(ButtonType.YES);
    }

    /**
     * Create a custom {@link Dialog} with title, header and buttons.
     * @param title title of the dialog
     * @param header header message of the dialog
     * @param buttons buttons to add on the dialog
     * @return the dialog
     * @param <R> type parameter of the dialog
     */
    public static <R> @NotNull Dialog<R> createCustomDialog(final String title, final String header,
                                                            final ButtonType... buttons) {
        final Dialog<R> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.getDialogPane().getButtonTypes().addAll(buttons);
        return dialog;
    }

    /**
     * Shows up an information {@link Notifications} with given title, text and setting its owner.
     * @param title title to use
     * @param text text to visualize
     * @param owner {@link Window} owning the notification
     */
    public static void showInfoNotification(final String title, final String text, final Window owner) {
        Notifications.create()
                .position(Pos.CENTER)
                .owner(owner)
                .title(title)
                .text(text)
                .showInformation();
    }

}
