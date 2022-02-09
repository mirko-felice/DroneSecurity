package it.unibo.dronesecurity.lib;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Provider of {@link Alert} utility methods.
 */
public final class AlertUtils {

    private AlertUtils() { }

    /**
     * Shows a Warning {@link Alert.AlertType} {@link Alert} without header.
     * @param contentMessage the message to show
     */
    public static void showWarningAlert(final String contentMessage) {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setHeaderText(null);
        alert.setContentText(contentMessage);
        alert.show();
    }

    /**
     * Shows an Error {@link Alert.AlertType} {@link Alert} without header.
     * @param contentMessage the message to show
     */
    public static void showErrorAlert(final String contentMessage) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(contentMessage);
        alert.show();
    }

    /**
     * Shows a Confirmation {@link Alert.AlertType} {@link Alert} without header.
     * @param title title of the alert
     * @param contentMessage the message to show
     * @return true if confirmation happens
     */
    public static boolean showConfirmationAlert(final String title, final String contentMessage) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contentMessage, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
        return alert.getResult().equals(ButtonType.YES);
    }

}
