/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities;

import io.github.dronesecurity.lib.Date;
import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.CourierImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.stage.Window;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
     * Creates drone picker dialog to select a drone from the list provided by {@link CourierImpl}.
     * WARNING: must be used only if the logged user is a Courier.
     * @param contentMessage message text to insert into the dialog
     * @param drones drones to show
     * @return the {@link Dialog} returning the drone identifier.
     */
    public static @NotNull Dialog<String> createDronePickerDialog(final String contentMessage,
                                                                  final List<String> drones) {
        final Dialog<String> dialog = createCustomDialog("Drone Selection",
                contentMessage, ButtonType.OK, ButtonType.CANCEL);

        final ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableList(drones));
        choiceBox.setValue(drones.get(0));
        dialog.getDialogPane().setContent(choiceBox);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK)
                return choiceBox.getValue();
            else
                return null;
        });
        return dialog;
    }

    /**
     * Creates a date picker dialog to select a date from tomorrow.
     * @return the {@link Dialog} returning the date selected as a {@link Date}
     */
    public static @NotNull Dialog<Date> createDatePickerDialog() {
        final Dialog<Date> dialog = DialogUtils.createCustomDialog("Reschedule Delivery",
                "Choose the new estimated arrival date", ButtonType.OK, ButtonType.CANCEL);

        final DatePicker datePicker = new DatePicker(LocalDate.now().plus(1, ChronoUnit.DAYS));
        datePicker.setDayCellFactory(ignored -> new OnlyFutureDateCell());
        dialog.getDialogPane().setContent(datePicker);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK)
                return Date.parseInstant(DateHelper.fromLocalDate(datePicker.getValue()));
            else
                return null;
        });
        return dialog;
    }

    /**
     * Shows up an information {@link Notifications} with given text and setting its owner.
     * @param text text to visualize
     * @param owner {@link Window} owning the notification
     */
    public static void showInfoNotification(final String text, final Window owner) {
        Notifications.create()
                .position(Pos.CENTER)
                .owner(owner)
                .title("INFO")
                .text(text)
                .showInformation();
    }

    private static <R> @NotNull Dialog<R> createCustomDialog(final String title, final String header,
                                                             final ButtonType... buttons) {
        final Dialog<R> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.getDialogPane().getButtonTypes().addAll(buttons);
        return dialog;
    }

    /**
     * {@link DatePicker} cell disabling date if it's chronologically before today.
     */
    private static class OnlyFutureDateCell extends DateCell {
        @Override
        public void updateItem(final LocalDate date, final boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.compareTo(LocalDate.now()) <= 0);
        }
    }
}
