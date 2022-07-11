/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.reporting.negligence;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.GenericUser;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.NegligenceConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller dedicated to control the detail node linked to a {@link org.controlsfx.control.MasterDetailPane}.
 */
public final class ReportDetailsUIController implements Initializable {

    private static final int CLEARABLE_LABEL_COLUMN_INDEX = 1;
    private static final String EMPTY_STRING = "";
    private static final String ARROW = " -> ";
    private static final String DEGREES = " \u00B0\t";
    @FXML private Label usernameLabel;
    @FXML private Label usernameValueLabel;
    @FXML private Label roleLabel;
    @FXML private Label roleValueLabel;

    @FXML private Label proximityLabel;
    @FXML private Label proximityValueLabel;
    @FXML private Label accelerometerLabel;
    @FXML private Label accelerometerValueLabel;
    @FXML private Label cameraLabel;
    @FXML private Label cameraValueLabel;

    @FXML private Label solutionLabel;
    @FXML private Text solutionText;

    private List<Node> userElements;
    private List<Node> dataElements;
    private List<Node> solutionElements;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.userElements = new ArrayList<>(
                Arrays.asList(this.usernameLabel, this.usernameValueLabel, this.roleLabel, this.roleValueLabel));
        this.dataElements = new ArrayList<>(
                Arrays.asList(this.proximityLabel, this.proximityValueLabel,
                        this.accelerometerLabel, this.accelerometerValueLabel,
                        this.cameraLabel, this.cameraValueLabel));
        this.solutionElements = new ArrayList<>(Arrays.asList(this.solutionLabel, this.solutionText));
    }

    /**
     * Update detail node using a {@link GenericUser}.
     * @param user user providing information
     */
    public void updateDetails(final @NotNull GenericUser user) {
        this.usernameValueLabel.setText(user.getUsername());
        this.roleValueLabel.setText(user.getRole().toString());
        this.userElements.forEach(n -> n.setVisible(true));
        this.dataElements.forEach(n -> n.setVisible(false));
        this.solutionElements.forEach(n -> n.setVisible(false));
    }

    /**
     * Update detail node using a {@link DroneData}.
     * @param data data object providing information
     */
    public void updateDetails(final @NotNull DroneData data) {
        this.proximityValueLabel.setText(data.getProximity() + " cm");
        final String angles = NegligenceConstants.ROLL
                + ARROW
                + data.getRoll()
                + DEGREES
                + NegligenceConstants.PITCH
                + ARROW
                + data.getPitch()
                + DEGREES
                + NegligenceConstants.YAW
                + ARROW
                + data.getYaw()
                + DEGREES;
        this.accelerometerValueLabel.setText(angles);
        this.cameraValueLabel.setText(data.getImageSize() + " bytes");
        this.userElements.forEach(n -> n.setVisible(false));
        this.dataElements.forEach(n -> n.setVisible(true));
        this.solutionElements.forEach(n -> n.setVisible(false));
    }

    /**
     * Update detail node using the action form of the report.
     * @param solution {@link NegligenceActionForm} related to the report
     */
    public void updateDetails(final @NotNull NegligenceActionForm solution) {
        this.solutionText.setText(solution.getSolution());
        this.userElements.forEach(n -> n.setVisible(false));
        this.dataElements.forEach(n -> n.setVisible(false));
        this.solutionElements.forEach(n -> n.setVisible(true));
    }

    /**
     * Empty the detail node.
     */
    public void emptyDetails() {
        this.userElements.forEach(n -> {
            n.setVisible(false);
            if (GridPane.getColumnIndex(n) == CLEARABLE_LABEL_COLUMN_INDEX)
                ((Label) n).setText(EMPTY_STRING);
        });
        this.dataElements.forEach(n -> {
            n.setVisible(false);
            if (GridPane.getColumnIndex(n) == CLEARABLE_LABEL_COLUMN_INDEX)
                ((Label) n).setText(EMPTY_STRING);
        });
        this.solutionElements.forEach(n -> {
            n.setVisible(false);
            if (n instanceof Text)
                ((Text) n).setText(EMPTY_STRING);
        });
    }
}
