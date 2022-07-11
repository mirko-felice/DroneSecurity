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
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.impl.ClosedNegligenceReportImpl;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.impl.OpenNegligenceReportImpl;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.*;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.NegligenceConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Deserialize {@link NegligenceReport} into {@link OpenNegligenceReport} or {@link ClosedNegligenceReport}.
 */
public final class NegligenceReportDeserializer extends JsonDeserializer<NegligenceReport> {

    /**
     * {@inheritDoc}
     */
    @Contract("_, _ -> new")
    @Override
    public @NotNull NegligenceReport deserialize(final @NotNull JsonParser parser,
                                                 final @NotNull DeserializationContext ctx) throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);

        final NegligenceIdentifier id = NegligenceIdentifier.fromLong(root.get(NegligenceConstants.ID).asLong());
        final Negligent negligent = Negligent.parse(root.get(NegligenceConstants.NEGLIGENT).asText());
        final Assignee assignee = Assignee.parse(root.get(NegligenceConstants.ASSIGNEE).asText());
        final DroneData data = mapper.readValue(root.get(NegligenceConstants.DATA).toString(), DroneData.class);

        final boolean isClosed = root.has(NegligenceConstants.ACTION_FORM);
        if (isClosed) {
            final NegligenceActionForm actionForm =
                    mapper.readValue(root.get(NegligenceConstants.ACTION_FORM).toString(), NegligenceActionForm.class);
            return new ClosedNegligenceReportImpl(id, negligent, assignee, data, actionForm);
        } else
            return new OpenNegligenceReportImpl(id, negligent, assignee, data);
    }

}
