/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.userapplication.common.data.entities.DroneData;
import io.github.dronesecurity.userapplication.common.data.serializers.DroneDataDeserializer;
import io.github.dronesecurity.userapplication.common.data.serializers.DroneDataSerializer;

/**
 * Immutable Value Object representing Drone Data.
 */
@JsonSerialize(using = DroneDataSerializer.class)
@JsonDeserialize(using = DroneDataDeserializer.class)
public interface NegligenceDroneData extends DroneData {

    /**
     * Copy this object.
     * @return a new fresh copy
     */
    @Override
    NegligenceDroneData deepCopy();

    /**
     * Checks if data are empty.
     * @return true if data are empty, false otherwise
     */
    boolean isEmpty();

}
