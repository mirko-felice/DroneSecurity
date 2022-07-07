/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.CourierImpl;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.MaintainerImpl;
import io.github.dronesecurity.userapplication.common.data.entities.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.serializers.NegligenceReportDeserializer;

import java.time.Instant;

/**
 * Represents the report used to record the commitment of a negligence by a {@link CourierImpl}.
 */
@JsonDeserialize(using = NegligenceReportDeserializer.class)
public interface NegligenceReport {

    /**
     * Gets the negligent as a {@link CourierImpl}.
     * @return the negligent
     */
    String getNegligent();

    /**
     * Gets the data collected in that instant.
     * @return the {@link DroneData} representing the data
     */
    DroneData getData();

    /**
     * Gets the {@link MaintainerImpl} assigned to the report.
     * @return the assignee
     */
    String assignedTo();

    /**
     * Gets the order identifier related to the negligence.
     * @return the order identifier
     */
    long getOrderId();

    /**
     * Gets the {@link Instant} when negligence has happened.
     * @return the negligence instant
     */
    Instant getNegligenceInstant();

}
