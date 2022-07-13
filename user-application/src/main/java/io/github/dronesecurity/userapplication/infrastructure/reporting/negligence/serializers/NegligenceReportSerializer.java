/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.NegligenceConstants;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link NegligenceReport} into the corresponding Json.
 */
public final class NegligenceReportSerializer extends JsonSerializer<NegligenceReport> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull NegligenceReport value, final @NotNull JsonGenerator gen,
                          final @NotNull SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(NegligenceConstants.ID, value.getId().asLong());
        gen.writeStringField(NegligenceConstants.NEGLIGENT, value.getNegligent().asString());
        gen.writeStringField(NegligenceConstants.ASSIGNEE, value.assignedTo().asString());
        gen.writeFieldName(NegligenceConstants.DATA);
        serializers.findValueSerializer(DroneData.class).serialize(value.getData(), gen, serializers);
        if (value instanceof ClosedNegligenceReport) {
            gen.writeFieldName(NegligenceConstants.ACTION_FORM);
            serializers.findValueSerializer(NegligenceActionForm.class)
                    .serialize(((ClosedNegligenceReport) value).getActionForm(), gen, serializers);
        }
        gen.writeEndObject();
        gen.flush();
    }
}
