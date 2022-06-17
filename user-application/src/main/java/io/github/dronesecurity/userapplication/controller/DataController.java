/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.userapplication.monitoring.UserMonitoringService;
import io.github.dronesecurity.userapplication.monitoring.entities.DroneData;
import io.github.dronesecurity.userapplication.monitoring.utilities.DataConstants;
import io.github.dronesecurity.userapplication.shipping.entities.Order;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller dedicated to show all the data related to an
 * {@link Order}.
 */
public class DataController implements Initializable {

    @FXML private Label title;
    @FXML private TableView<DroneData> dataTable;
    @FXML private TableColumn<DroneData, String> instantColumn;
    @FXML private TableColumn<DroneData, Map<String, Integer>> accelerometerColumn;
    @FXML private TableColumn<DroneData, Double> proximityColumn;
    @FXML private TableColumn<DroneData, Integer> rollColumn;
    @FXML private TableColumn<DroneData, Integer> pitchColumn;
    @FXML private TableColumn<DroneData, Integer> yawColumn;
    @FXML private TableColumn<DroneData, Long> cameraColumn;
    private final long orderId;

    /**
     * Build the controller.
     * @param orderId {@link Order} identifier to
     *                                                                                               retrieve data from
     */
    public DataController(final long orderId) {
        this.orderId = orderId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.title.setText(this.title.getText() + this.orderId);

        this.dataTable.setSelectionModel(null);

        this.instantColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(DateHelper.toString(cell.getValue().getDetectionInstant())));
        this.instantColumn.setReorderable(false);

        this.proximityColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getProximity()));
        this.proximityColumn.setCellFactory(ignored -> new FXHelper.ProximityCell<>());
        this.proximityColumn.setReorderable(false);

        this.accelerometerColumn.setReorderable(false);

        this.rollColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getAccelerometer().get(DataConstants.ROLL)));
        this.rollColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.rollColumn.setReorderable(false);

        this.pitchColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getAccelerometer().get(DataConstants.PITCH)));
        this.pitchColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.pitchColumn.setReorderable(false);

        this.yawColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getAccelerometer().get(DataConstants.YAW)));
        this.yawColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.yawColumn.setReorderable(false);

        this.cameraColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getCamera()));
        this.cameraColumn.setCellFactory(ignored -> new FXHelper.CameraCell<>());
        this.cameraColumn.setReorderable(false);

        new UserMonitoringService(this.orderId).retrieveDataHistory().onSuccess(data -> Platform.runLater(() ->
                this.dataTable.setItems(FXCollections.observableList(data))));
    }
}
