/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.shared.Date;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.NegligenceConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Deserialize Json into {@link NegligenceActionForm}.
 */
public final class NegligenceActionFormDeserializer extends JsonDeserializer<NegligenceActionForm> {

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull NegligenceActionForm deserialize(final @NotNull JsonParser parser,
                                                     final DeserializationContext ctxt) throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);
        final String solution = root.get(NegligenceConstants.SOLUTION).asText();
        if (root.has(NegligenceConstants.CLOSING_INSTANT)) {
            final Date closingInstant = Date.parseString(root.get(NegligenceConstants.CLOSING_INSTANT).asText());
            return NegligenceActionForm.parse(solution, closingInstant);
        } else
            return NegligenceActionForm.create(solution);
    }
}
