/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.serializers.NegligenceReportWithIDSerializer;

/**
 * Represents a {@link NegligenceReport} adding unique identifier.
 */
@JsonSerialize(using = NegligenceReportWithIDSerializer.class)
public interface NegligenceReportWithID extends NegligenceReport {

    /**
     * Gets the unique ID associated with this report.
     * @return the ID
     */
    long getId();
}
