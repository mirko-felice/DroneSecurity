/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.drone.usermonitoring;

import io.github.dronesecurity.userapplication.application.drone.usermonitoring.DataManagerImpl;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.AccelerometerData;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.services.DataManager;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.repo.DataRepositoryImpl;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
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
 * Controller dedicated to show all the accelerometer data related to an order.
 */
public final class AccelerometerDataController implements Initializable {

    @FXML private Label title;
    @FXML private TableView<AccelerometerData> dataTable;
    @FXML private TableColumn<AccelerometerData, String> instantColumn;
    @FXML private TableColumn<AccelerometerData, Map<String, Integer>> accelerometerColumn;
    @FXML private TableColumn<AccelerometerData, Integer> rollColumn;
    @FXML private TableColumn<AccelerometerData, Integer> pitchColumn;
    @FXML private TableColumn<AccelerometerData, Integer> yawColumn;

    private final long orderId;
    private final DataManager dataManager;

    /**
     * Build the controller.
     * @param orderId order identifier to retrieve data history from
     */
    public AccelerometerDataController(final long orderId) {
        this.orderId = orderId;
        this.dataManager = new DataManagerImpl(new DataRepositoryImpl());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.title.setText(this.title.getText() + this.orderId);

        this.dataTable.setSelectionModel(null);

        this.instantColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getDetectionInstant().asString()));
        this.instantColumn.setReorderable(false);

        this.accelerometerColumn.setReorderable(false);

        this.pitchColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getPitch()));
        this.pitchColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.pitchColumn.setReorderable(false);

        this.rollColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getRoll()));
        this.rollColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.rollColumn.setReorderable(false);

        this.yawColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getYaw()));
        this.yawColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.yawColumn.setReorderable(false);

        this.dataTable.setItems(FXCollections
                .observableList(this.dataManager.retrieveAccelerometerDataHistory(this.orderId)));
    }
}
