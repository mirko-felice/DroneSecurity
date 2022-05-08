package it.unibo.dronesecurity.userapplication.controller;

import it.unibo.dronesecurity.userapplication.auth.entities.LoggedUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

/**
 * Controller dedicated to control the detail node linked to a {@link org.controlsfx.control.MasterDetailPane}.
 */
public class DetailController {

    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;

    /**
     * Update detail node using a {@link LoggedUser}.
     * @param user user providing information
     */
    public void updateDetails(final @NotNull LoggedUser user) {
        this.usernameLabel.setText(user.getUsername());
        this.roleLabel.setText(user.getRole().toString());
    }
}
