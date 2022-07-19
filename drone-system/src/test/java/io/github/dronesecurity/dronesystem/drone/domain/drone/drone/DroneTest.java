/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.drone;

import io.github.dronesecurity.dronesystem.drone.domain.drone.drone.entities.Drone;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.DrivingMode;
import org.junit.jupiter.api.*;

/**
 * Test for Drone Service.
 */
class DroneTest {

    private static final int ORDER_ID = 1;
    private static final String COURIER = "courier";
    private static final String DRONE_ID = "Test Drone";

    private Drone drone;

    /**
     * Instantiates the drone for the tests.
     */
    /* default */ DroneTest() {
        this.drone = new Drone(DRONE_ID);
    }

    /**
     * Opens the connection.
     */
    @BeforeAll
    /* default */ static void openConnection() {
        Connection.getInstance().connect();
    }

    /**
     * Closes the connection.
     */
    @AfterAll
    /* default */ static void closeConnection() {
        Connection.getInstance().closeConnection();
    }

    /**
     * Activates the drone before executing each test.
     */
    @BeforeEach
    /* default */ void startDrone() {
        final OrderData orderData = new OrderData(ORDER_ID, COURIER);
        this.drone = new Drone(DRONE_ID);

        this.drone.activate(orderData);
        Assertions.assertTrue(this.drone.isOperating(), "After activation the drone should be operating.");
    }

    /**
     * Deactivates the drone after each test.
     */
    @AfterEach
    /* default */ void deactivateDrone() {
        this.drone.deactivate();
        Assertions.assertFalse(this.drone.isOperating(), "Drone should NOT be operative after deactivation.");
        Connection.getInstance().closeConnection();
    }

    /**
     * Tests drone's moving changing.
     */
    @Test
    void testDroneModeChange() {
        this.drone.changeMode(DrivingMode.MANUAL);
        Assertions.assertFalse(this.drone.isOperating(),
                "If driving mode is MANUAL the drone should NOT be moving automatically.");

        this.drone.proceed();
        Assertions.assertFalse(this.drone.isOperating(),
                "If driving mode is MANUAL the drone should NOT be started.");

        this.drone.changeMode(DrivingMode.AUTOMATIC);
        Assertions.assertFalse(this.drone.isOperating(),
                "Drone should NOT be started instantly after changing mode to automatic.");

        this.drone.proceed();
        Assertions.assertTrue(this.drone.isOperating(), "The drone should start if he is in automatic mode.");
    }

    /**
     * Tests drone halting/proceeding.
     */
    @Test
    void testDroneMovingStateChange() {
        this.drone.halt();
        Assertions.assertFalse(this.drone.isOperating(), "Drone should be stopped after halting it.");

        this.drone.proceed();
        Assertions.assertTrue(this.drone.isOperating(), "The drone should start after it's ordered to proceed.");
    }
}