package it.unibo.dronesecurity.userapplication.utilities;

import javafx.scene.control.Alert;

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
}
