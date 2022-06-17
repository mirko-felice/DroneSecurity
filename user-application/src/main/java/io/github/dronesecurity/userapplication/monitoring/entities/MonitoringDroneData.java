/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.monitoring.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.userapplication.monitoring.serializers.MonitoringDroneDataDeserializer;
import io.github.dronesecurity.userapplication.monitoring.serializers.MonitoringDroneDataSerializer;
import io.github.dronesecurity.userapplication.common.data.entities.DroneData;

import java.time.Instant;

/**
 * Immutable Value Object representing Drone Data.
 */
@JsonSerialize(using = MonitoringDroneDataSerializer.class)
@JsonDeserialize(using = MonitoringDroneDataDeserializer.class)
public interface MonitoringDroneData extends DroneData {

    /**
     * Gets the instant when detection is happened.
     * @return the {@link Instant}
     */
    Instant getDetectionInstant();

    /**
     * Gets the order identifier related to this data detection.
     * @return the order identifier
     */
    long getOrderId();

}
