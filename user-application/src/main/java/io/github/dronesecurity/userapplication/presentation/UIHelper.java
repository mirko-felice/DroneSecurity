/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation;

import io.github.dronesecurity.userapplication.presentation.drone.usermonitoring.AccelerometerDataController;
import io.github.dronesecurity.userapplication.presentation.drone.usermonitoring.CameraDataController;
import io.github.dronesecurity.userapplication.presentation.drone.usermonitoring.DroneController;
import io.github.dronesecurity.userapplication.presentation.drone.usermonitoring.ProximityDataController;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
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
    private static final String MONITORING_FXML = "drone/usermonitoring/dataMonitoring.fxml";

    private static final double DRONE_CONTROLLER_MIN_WIDTH = 500;
    private static final double DRONE_CONTROLLER_MIN_HEIGHT = 200;
    private static final String DRONE_CONTROLLER_FXML = "drone/usermonitoring/droneController.fxml";

    private static final double PROXIMITY_DATA_MIN_WIDTH = 400;
    private static final double PROXIMITY_DATA_MIN_HEIGHT = 300;
    private static final String PROXIMITY_DATA_FXML = "drone/usermonitoring/proximityData.fxml";

    private static final double ACCELEROMETER_DATA_MIN_WIDTH = 550;
    private static final double ACCELEROMETER_DATA_MIN_HEIGHT = 300;
    private static final String ACCELEROMETER_DATA_FXML = "drone/usermonitoring/accelerometerData.fxml";

    private static final double CAMERA_DATA_MIN_WIDTH = 400;
    private static final double CAMERA_DATA_MIN_HEIGHT = 200;
    private static final String CAMERA_DATA_FXML = "drone/usermonitoring/cameraData.fxml";

    private UIHelper() { }

    /**
     * Shows up the Monitoring GUI related to the delivering order.
     */
    public static void showMonitoringUI() {
        final URL fileUrl = UIHelper.class.getResource(MONITORING_FXML);
        final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Monitoring...", fxmlLoader,
                MONITORING_MIN_WIDTH, MONITORING_MIN_HEIGHT).ifPresent(stage -> {
                    stage.setOnCloseRequest(Event::consume);
                    stage.show();
                });
    }

    /**
     * Shows up the Monitoring GUI related to the delivering order.
     * @param orderId order identifier to monitor
     * @param droneId drone identifier to control
     */
    public static void showDroneControllerUI(final long orderId, final String droneId) {
        final URL fileUrl = UIHelper.class.getResource(DRONE_CONTROLLER_FXML);
        final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
        fxmlLoader.setController(new DroneController(orderId, droneId));
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Drone Controller", fxmlLoader,
                DRONE_CONTROLLER_MIN_WIDTH, DRONE_CONTROLLER_MIN_HEIGHT).ifPresent(stage -> {
            stage.setOnCloseRequest(Event::consume);
            stage.show();
        });
    }

    /**
     * Shows up the Proximity Data History GUI related to the given order.
     * @param orderId order identifier to retrieve data history from
     */
    public static void showProximityDataHistoryUI(final long orderId) {
        final FXMLLoader loader = new FXMLLoader(UIHelper.class.getResource(PROXIMITY_DATA_FXML));
        loader.setController(new ProximityDataController(orderId));
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Proximity Data History", loader,
                        PROXIMITY_DATA_MIN_WIDTH, PROXIMITY_DATA_MIN_HEIGHT)
                .ifPresent(Stage::show);
    }

    /**
     * Shows up the Accelerometer Data History GUI related to the given order.
     * @param orderId order identifier to retrieve data history from
     */
    public static void showAccelerometerDataHistoryUI(final long orderId) {
        final FXMLLoader loader = new FXMLLoader(UIHelper.class.getResource(ACCELEROMETER_DATA_FXML));
        loader.setController(new AccelerometerDataController(orderId));
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Accelerometer Data History", loader,
                        ACCELEROMETER_DATA_MIN_WIDTH, ACCELEROMETER_DATA_MIN_HEIGHT)
                .ifPresent(Stage::show);
    }

    /**
     * Shows up the Camera Data History GUI related to the given order.
     * @param orderId order identifier to retrieve data history from
     */
    public static void showCameraDataHistoryUI(final long orderId) {
        final FXMLLoader loader = new FXMLLoader(UIHelper.class.getResource(CAMERA_DATA_FXML));
        loader.setController(new CameraDataController(orderId));
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Camera Data History", loader,
                        CAMERA_DATA_MIN_WIDTH, CAMERA_DATA_MIN_HEIGHT)
                .ifPresent(Stage::show);
    }
}
