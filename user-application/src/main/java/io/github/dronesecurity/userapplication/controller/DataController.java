/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.drone.monitoring.UserMonitoringService;
import io.github.dronesecurity.userapplication.drone.monitoring.entities.DroneData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller dedicated to show all the data related to an
 * {@link io.github.dronesecurity.userapplication.shipping.courier.entities.Order}.
 */
public class DataController implements Initializable {

    @FXML private TableView<DroneData> dataTable;
    private final String orderId;

    /**
     * Build the controller.
     * @param orderId {@link io.github.dronesecurity.userapplication.shipping.courier.entities.Order} identifier to
     *                                                                                               retrieve data from
     */
    public DataController(final String orderId) {
        this.orderId = orderId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        new UserMonitoringService(this.orderId).retrieveDataHistory().onSuccess(data -> Platform.runLater(() ->
                this.dataTable.setItems(FXCollections.observableList(data))));
    }
}
