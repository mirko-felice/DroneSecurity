/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
import io.github.dronesecurity.userapplication.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.negligence.entities.NegligenceReportWithID;
import io.github.dronesecurity.userapplication.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;

/**
 * Serialize {@link NegligenceReportWithID} into the corresponding Json.
 */
public class NegligenceReportWithIDSerializer extends JsonSerializer<NegligenceReportWithID> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull NegligenceReportWithID value, final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(NegligenceConstants.ID, value.getId());
        gen.writeStringField(NegligenceConstants.NEGLIGENT, value.getNegligent());
        gen.writeStringField(NegligenceConstants.ASSIGNEE, value.assignedTo());
        gen.writeObjectField(NegligenceConstants.DATA, value.getData());
        if (value instanceof ClosedNegligenceReport) {
            final Instant closingInstant = ((ClosedNegligenceReport) value).getClosingInstant();
            gen.writeStringField(NegligenceConstants.CLOSING_INSTANT, DateHelper.toString(closingInstant));
        }
        gen.writeEndObject();
        gen.flush();
    }
}
