/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone;

import io.github.dronesecurity.dronesystem.drone.domain.drone.drone.entities.Drone;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.connection.MqttMessageValueConstants;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service that simulates a delivery.
 */
public class NavigationService {

    private static final long TRAVELING_TIME = 6000;
    private static final long TRAVEL_SIMULATION_DELAY = 50;
    private static final int RANDOM_GENERATION_RANGE = 100;
    private static final int SUCCESS_PERCENTAGE = 70;

    private final SecureRandom randomGenerator;
    private final ScheduledExecutorService deliveryExecutor;
    private final ScheduledExecutorService returnExecutor;

    private final Drone drone;
    private final OrderData orderData;

    private final DeliveryStatusPublisher deliveryStatusPublisher;

    /**
     * Instantiates the Navigation Service with a specific drone and data of the order to deliver.
     *
     * @param drone the drone to operate on
     * @param orderData the data relative to the order to deliver
     */
    public NavigationService(final Drone drone, final OrderData orderData) {
        this.drone = drone;
        this.orderData = orderData;
        this.randomGenerator = new SecureRandom();
        this.deliveryExecutor = Executors.newSingleThreadScheduledExecutor();
        this.returnExecutor = Executors.newSingleThreadScheduledExecutor();

        this.deliveryStatusPublisher = new DeliveryStatusPublisher();
    }

    /**
     * Starts the delivery simulation.
     */
    public void start() {
        this.deliveryStatusPublisher.publishCurrentStatus(this.orderData, MqttMessageValueConstants.DELIVERING_MESSAGE);
        this.travelSimulation(this.deliveryExecutor, this::publishDelivery);
    }

    /**
     * Starts the returning journey simulation.
     *
     * @param closingAction the action to perform after the delivery is over
     */
    public void callback(final Runnable closingAction) {
        this.deliveryStatusPublisher
                .publishCurrentStatus(this.orderData, MqttMessageValueConstants.RETURNING_ACKNOWLEDGEMENT_MESSAGE);
        this.travelSimulation(this.returnExecutor, () -> this.termination(closingAction));
    }

    private void publishDelivery() {
        final int choice = this.randomGenerator.nextInt(RANDOM_GENERATION_RANGE - 1);
        if (choice < SUCCESS_PERCENTAGE)
            this.deliveryStatusPublisher.publishCurrentStatus(this.orderData,
                    MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE);
        else
            this.deliveryStatusPublisher.publishCurrentStatus(this.orderData,
                    MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE);
        this.drone.halt();
    }

    private void termination(final @NotNull Runnable terminatingAction) {
        this.deliveryStatusPublisher.publishCurrentStatus(this.orderData,
                MqttMessageValueConstants.RETURNED_ACKNOWLEDGEMENT_MESSAGE);
        terminatingAction.run();
    }

    private void travelSimulation(final @NotNull ScheduledExecutorService executor, final Runnable terminationAction) {
        executor.scheduleWithFixedDelay(new TravelSimulator(executor, terminationAction),
                0, TRAVEL_SIMULATION_DELAY, TimeUnit.MILLISECONDS);
    }

    /**
     * Travel simulator.
     */
    private final class TravelSimulator implements Runnable {

        private final ScheduledExecutorService executor;
        private final Runnable terminationAction;
        private Instant startingInstant = Instant.now();
        private long remainingTravelTime = TRAVELING_TIME;
        private Instant deliveredMoment = this.startingInstant.plusMillis(this.remainingTravelTime);
        private boolean wasStopped;

        private TravelSimulator(final @NotNull ScheduledExecutorService executor, final Runnable terminationAction) {
            this.executor = executor;
            this.terminationAction = terminationAction;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            if (NavigationService.this.drone.isOperating()) {
                if (this.wasStopped) {
                    this.startingInstant = Instant.now();
                    this.deliveredMoment = this.startingInstant.plusMillis(this.remainingTravelTime);
                    this.wasStopped = false;
                } else if (Instant.now().isAfter(this.deliveredMoment)) {
                    this.terminationAction.run();
                    this.executor.shutdownNow();
                }
            } else if (!this.wasStopped && !NavigationService.this.drone.isOperating()) {
                this.wasStopped = true;
                final long timeElapsed = Instant.now().toEpochMilli() - this.startingInstant.toEpochMilli();
                this.remainingTravelTime -= timeElapsed;
            }
        }
    }
}
