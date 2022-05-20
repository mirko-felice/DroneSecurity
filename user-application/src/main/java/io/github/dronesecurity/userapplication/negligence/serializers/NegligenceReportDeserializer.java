/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.userapplication.negligence.entities.*;
import io.github.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
import io.github.dronesecurity.userapplication.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;

/**
 * Deserialize {@link NegligenceReport} into {@link OpenNegligenceReport} or {@link ClosedNegligenceReport}.
 */
public final class NegligenceReportDeserializer extends JsonDeserializer<NegligenceReport> {

    @Override
    public @NotNull NegligenceReport deserialize(@NotNull final JsonParser parser,
                                                 final @NotNull DeserializationContext ctx) throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);

        final String negligent = root.get(NegligenceConstants.NEGLIGENT).asText();
        final String assignee = (String) ctx.getAttribute(NegligenceConstants.ASSIGNEE);
        final String maintainer = assignee == null ? root.get(NegligenceConstants.ASSIGNEE).asText() : assignee;

        final DroneData data = new DroneDataImpl(root.get(NegligenceConstants.DATA));

        final NegligenceReport report = NegligenceReportFactory.withoutID(negligent, maintainer, data);
        if (!root.has(NegligenceConstants.ID))
            return report;

        final boolean isClosed = root.has(NegligenceConstants.CLOSING_INSTANT);
        final long id = root.get(NegligenceConstants.ID).asLong();
        final NegligenceReportWithID withID = NegligenceReportFactory.withID(id, report);
        if (isClosed) {
            final Instant closingInstant =
                    DateHelper.toInstant(root.get(NegligenceConstants.CLOSING_INSTANT).asText());
            return NegligenceReportFactory.closed(withID, closingInstant);
        } else
            return NegligenceReportFactory.open(withID);
    }

}
