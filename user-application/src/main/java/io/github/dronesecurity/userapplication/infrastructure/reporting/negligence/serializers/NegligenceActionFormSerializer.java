/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.NegligenceConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link NegligenceActionForm} into Json.
 */
public final class NegligenceActionFormSerializer extends JsonSerializer<NegligenceActionForm> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(final @NotNull NegligenceActionForm value,
                          final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(NegligenceConstants.SOLUTION, value.getSolution());
        gen.writeStringField(NegligenceConstants.CLOSING_INSTANT, value.getClosingInstant().asString());
        gen.writeEndObject();
        gen.flush();
    }
}
