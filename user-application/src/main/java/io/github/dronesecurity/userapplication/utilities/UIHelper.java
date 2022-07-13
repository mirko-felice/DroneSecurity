/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.presentation.DataController;
import io.github.dronesecurity.userapplication.presentation.MonitorController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Helper to show particular GUIs.
 */
public final class UIHelper {

    private static final double MONITORING_MIN_WIDTH = 800;
    private static final double MONITORING_MIN_HEIGHT = 700;
    private static final String MONITORING_FXML = "monitoring.fxml";


    private static final double DATA_MIN_WIDTH = 850;
    private static final double DATA_MIN_HEIGHT = 500;
    private static final String DATA_FXML = "data.fxml";

    private UIHelper() { }

    /**
     * Shows up the Monitoring GUI related to the delivering order.
     * @param order {@link Order} to monitor
     */
    public static void showMonitoringUI(final Order order) {
        final URL fileUrl = UIHelper.class.getResource(MONITORING_FXML);
        final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
        fxmlLoader.setController(new MonitorController(order));
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Monitoring...", fxmlLoader,
                MONITORING_MIN_WIDTH, MONITORING_MIN_HEIGHT).ifPresent(stage -> {
                    stage.setOnCloseRequest(Event::consume);
                    stage.show();
                });
    }

    /**
     * Shows up the Data History GUI related to the given order.
     * @param order {@link Order} to retrieve data history from
     */
    public static void showDataHistoryUI(final Order order) {
        final FXMLLoader loader = new FXMLLoader(UIHelper.class.getResource(DATA_FXML));
        loader.setController(new DataController(order));
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Data History", loader, DATA_MIN_WIDTH, DATA_MIN_HEIGHT)
                .ifPresent(Stage::show);
    }
}
