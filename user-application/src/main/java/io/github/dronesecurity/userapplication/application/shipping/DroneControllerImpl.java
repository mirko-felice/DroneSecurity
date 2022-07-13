/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.shipping;

import io.github.dronesecurity.lib.DrivingMode;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderDelivering;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.service.DroneController;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link DroneController}.
 */
public final class DroneControllerImpl implements DroneController {

    /**
     * Build the drone controller.
     */
    public DroneControllerImpl() {
        DomainEvents.register(OrderDelivering.class, this::startDrone);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void callBack(final @NotNull OrderIdentifier orderId) {
        ConnectionHelper.sendCallBackMessage(orderId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeDrivingMode(final OrderIdentifier orderId, final DrivingMode drivingMode) {
        ConnectionHelper.sendChangeModeMessage(orderId, drivingMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void proceed(final OrderIdentifier orderId) {
        ConnectionHelper.sendProceedMessage(orderId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void halt(final OrderIdentifier orderId) {
        ConnectionHelper.sendHaltMessage(orderId);
    }

    private void startDrone(final @NotNull OrderDelivering orderDelivering) {
        final String courierUsername = orderDelivering.getCourierUsername();
        ConnectionHelper.sendPerformDeliveryMessage(
                orderDelivering.getDroneId(),
                orderDelivering.getOrder(),
                courierUsername);
    }
}
