package it.unibo.dronesecurity.userapplication.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.userapplication.auth.entities.LoggedUser;
import it.unibo.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller dedicated to control the detail node linked to a {@link org.controlsfx.control.MasterDetailPane}.
 */
public class DetailController implements Initializable {

    @FXML private Label usernameLabel;
    @FXML private Label usernameValueLabel;
    @FXML private Label roleLabel;
    @FXML private Label roleValueLabel;

    @FXML private Label proximityLabel;
    @FXML private Label proximityValueLabel;
    private List<Node> userElements;
    private List<Node> dataElements;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.userElements = new ArrayList<>(
                Arrays.asList(this.usernameLabel, this.usernameValueLabel, this.roleLabel, this.roleValueLabel));
        this.dataElements = new ArrayList<>(
                Arrays.asList(this.proximityLabel, this.proximityValueLabel));
    }

    /**
     * Update detail node using a {@link LoggedUser}.
     * @param user user providing information
     */
    public void updateDetails(final @NotNull LoggedUser user) {
        this.usernameValueLabel.setText(user.getUsername());
        this.roleValueLabel.setText(user.getRole().toString());
        this.userElements.forEach(n -> n.setVisible(true));
        this.dataElements.forEach(n -> n.setVisible(false));
    }

    /**
     * Update detail node using a {@link ObjectNode}.
     * @param data data object providing information
     */
    public void updateDetails(final @NotNull ObjectNode data) {
        this.proximityValueLabel.setText(data.get(NegligenceConstants.PROXIMITY).asText());
        this.userElements.forEach(n -> n.setVisible(false));
        this.dataElements.forEach(n -> n.setVisible(true));
    }
}
