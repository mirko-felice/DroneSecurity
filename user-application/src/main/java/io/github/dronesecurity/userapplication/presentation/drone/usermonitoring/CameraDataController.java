/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.drone.usermonitoring;

import io.github.dronesecurity.userapplication.application.drone.usermonitoring.DataManagerImpl;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects.CameraData;
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
import java.util.ResourceBundle;

/**
 * Controller dedicated to show all the camera data related to an order.
 */
public final class CameraDataController implements Initializable {

    @FXML private Label title;
    @FXML private TableView<CameraData> dataTable;
    @FXML private TableColumn<CameraData, String> instantColumn;
    @FXML private TableColumn<CameraData, Long> cameraColumn;

    private final long orderId;
    private final DataManager dataManager;

    /**
     * Build the controller.
     * @param orderId order identifier to retrieve data history from
     */
    public CameraDataController(final long orderId) {
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

        this.cameraColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>((long) cell.getValue().getImageSize()));
        this.cameraColumn.setCellFactory(ignored -> new FXHelper.CameraCell<>());
        this.cameraColumn.setReorderable(false);


        this.dataTable.setItems(FXCollections
                .observableList(this.dataManager.retrieveCameraDataHistory(this.orderId)));
    }
}
