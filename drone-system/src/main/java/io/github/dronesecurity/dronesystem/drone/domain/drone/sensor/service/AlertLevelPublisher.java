/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.service;

import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.shared.AlertLevel;

/**
 * Class defining a service that publishes drone's {@link AlertLevel}.
 */
public interface AlertLevelPublisher {

    /**
     * Publish the current {@link AlertLevel} on the related topic.
     * @param orderData order identifier needed in order to publish correctly
     * @param currentAlert current alert to publish
     */
    void publishCurrentAlertLevel(OrderData orderData, Alert currentAlert);

    /**
     * Publish the Stable {@link AlertLevel} on the related topic.
     * @param orderData order identifier needed in order to publish correctly
     */
    void publishStableAlertLevel(OrderData orderData);
}
