/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.drone.usermonitoring;

import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.connection.MqttMessageValueConstants;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.AccelerometerRead;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.CameraRead;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.ProximityRead;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.StatusChanged;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.events.NewNegligence;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller dedicated to monitoring delivery.
 */
public final class MonitorController implements Initializable {

    @FXML private Label proximityCurrentData;
    @FXML private TableView<Map<String, Integer>> accelerometerCurrentData;
    @FXML private Label cameraCurrentData;

    @FXML private TableView<Double> proximityPreviousData;
    @FXML private TableView<Map<String, Integer>> accelerometerPreviousData;
    @FXML private TableView<Long> cameraPreviousData;

    @FXML private TableColumn<Double, Double> proximityPreviousDataColumn;
    @FXML private TableColumn<Map<String, Integer>, Integer> accelerometerPreviousXDataColumn;
    @FXML private TableColumn<Map<String, Integer>, Integer> accelerometerPreviousYDataColumn;
    @FXML private TableColumn<Map<String, Integer>, Integer> accelerometerPreviousZDataColumn;
    @FXML private TableColumn<Long, Long> cameraPreviousDataColumn;

    @FXML private TableColumn<Map<String, Integer>, Integer> currentAccelerometerXValue;
    @FXML private TableColumn<Map<String, Integer>, Integer> currentAccelerometerYValue;
    @FXML private TableColumn<Map<String, Integer>, Integer> currentAccelerometerZValue;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        DomainEvents.register(NewNegligence.class, this::onNewNegligence);
        DomainEvents.register(ProximityRead.class, this::onProximityRead);
        DomainEvents.register(AccelerometerRead.class, this::onAccelerometerRead);
        DomainEvents.register(CameraRead.class, this::onCameraRead);
        DomainEvents.register(StatusChanged.class, event -> {
            if (MqttMessageValueConstants.RETURNED_ACKNOWLEDGEMENT_MESSAGE.equals(event.getStatus()))
                Platform.runLater(((Stage) this.proximityCurrentData.getScene().getWindow())::close);
        });

        this.proximityPreviousDataColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        this.proximityPreviousDataColumn.setCellFactory(ignored -> new FXHelper.ProximityCell<>());
        this.proximityPreviousDataColumn.setReorderable(false);

        this.accelerometerPreviousXDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.ROLL)));
        this.accelerometerPreviousXDataColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.accelerometerPreviousXDataColumn.setReorderable(false);

        this.accelerometerPreviousYDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.PITCH)));
        this.accelerometerPreviousYDataColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.accelerometerPreviousYDataColumn.setReorderable(false);

        this.accelerometerPreviousZDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.YAW)));
        this.accelerometerPreviousZDataColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.accelerometerPreviousZDataColumn.setReorderable(false);

        this.cameraPreviousDataColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        this.cameraPreviousDataColumn.setCellFactory(ignored -> new FXHelper.CameraCell<>());
        this.cameraPreviousDataColumn.setReorderable(false);

        this.currentAccelerometerXValue.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.ROLL)));
        this.currentAccelerometerXValue.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.currentAccelerometerXValue.setReorderable(false);

        this.currentAccelerometerYValue.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.PITCH)));
        this.currentAccelerometerYValue.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.currentAccelerometerYValue.setReorderable(false);

        this.currentAccelerometerZValue.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.YAW)));
        this.currentAccelerometerZValue.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.currentAccelerometerZValue.setReorderable(false);
    }

    private void onNewNegligence(final NewNegligence newNegligence) {
        Platform.runLater(() -> DialogUtils.showInfoNotification(
                "You have committed a negligence. The drone has been halted for security purpose."
                        + "\nMaintainer " + newNegligence.getReport().assignedTo()
                        + " will take care of this. Go to the 'reports' window to show more information about it.",
                this.proximityCurrentData.getScene().getWindow()));
    }

    private void onProximityRead(final ProximityRead proximityRead) {
        Platform.runLater(() -> {
            if (!this.proximityCurrentData.getText().isEmpty())
                this.proximityPreviousData.getItems().add(0,
                        Double.valueOf(this.proximityCurrentData.getText().split(" ")[0]));

            this.proximityCurrentData.setText(proximityRead.getProximity().getDistance() + " cm");
        });
    }

    private void onAccelerometerRead(final AccelerometerRead accelerometerRead) {
        Platform.runLater(() -> {
            final ObservableList<Map<String, Integer>> accelerometerValues = this.accelerometerCurrentData.getItems();
            if (!accelerometerValues.isEmpty())
                this.accelerometerPreviousData.getItems().add(0, accelerometerValues.get(0));

            accelerometerValues.clear();
            accelerometerValues.add(accelerometerRead.getAccelerometerData().asMap());
        });
    }

    private void onCameraRead(final CameraRead cameraRead) {
        Platform.runLater(() -> {
            if (!this.cameraCurrentData.getText().isEmpty())
                this.cameraPreviousData.getItems().add(0,
                        Long.valueOf(this.cameraCurrentData.getText().split(" ")[0]));

            this.cameraCurrentData.setText(cameraRead.getCameraData().getImageSize() + " bytes");
        });
    }
}
