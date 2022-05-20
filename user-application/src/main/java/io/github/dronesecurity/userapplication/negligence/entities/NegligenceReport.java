/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.negligence.serializers.NegligenceReportDeserializer;

/**
 * Represents the report used to record the commitment of a negligence by a {@link Courier}.
 */
@JsonDeserialize(using = NegligenceReportDeserializer.class)
public interface NegligenceReport {

    /**
     * Gets the negligent as a {@link Courier}.
     * @return the negligent
     */
    String getNegligent();

    /**
     * Gets the data collected in that instant.
     * @return the {@link ObjectNode} representing the data
     */
    DroneData getData();

    /**
     * Gets the {@link Maintainer} assigned to the report.
     * @return the assignee
     */
    String assignedTo();

}
