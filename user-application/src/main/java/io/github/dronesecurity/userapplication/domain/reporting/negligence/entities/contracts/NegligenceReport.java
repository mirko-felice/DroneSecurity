/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.lib.shared.Entity;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceIdentifier;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers.NegligenceReportDeserializer;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers.NegligenceReportSerializer;

/**
 * {@link Entity} representing the report used to record the commitment of a negligence.
 */
@JsonSerialize(using = NegligenceReportSerializer.class)
@JsonDeserialize(using = NegligenceReportDeserializer.class)
public interface NegligenceReport extends Entity<NegligenceReport> {

    /**
     * Gets the report identifier.
     * @return the {@link NegligenceIdentifier}
     */
    NegligenceIdentifier getId();

    /**
     * Gets the {@link Negligent} of the report.
     * @return the {@link Negligent}
     */
    Negligent getNegligent();

    /**
     * Gets the {@link Assignee} assigned to the report.
     * @return the {@link Assignee}
     */
    Assignee assignedTo();

    /**
     * Gets the data collected in that instant.
     * @return the {@link DroneData} representing the data
     */
    DroneData getData();

}
