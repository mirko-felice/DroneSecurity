/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.userapplication.shipping.courier.entities.*;
import io.github.dronesecurity.userapplication.shipping.courier.utilities.OrderConstants;
import io.github.dronesecurity.lib.DateHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Instant;

/**
 * Deserialize {@link Order} into the correct implementation basing on the current state of it.
 */
public final class OrderDeserializer extends JsonDeserializer<Order> {

    @Override
    public @Nullable Order deserialize(@NotNull final JsonParser parser, final DeserializationContext ctx)
            throws IOException {
        final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        final ObjectNode root = mapper.readTree(parser);
        if (!root.has(OrderConstants.EVENTS))
            return null;
        final JsonNode events = root.get(OrderConstants.EVENTS);
        final String currentState = events.get(events.size() - 1).asText();
        final String id = root.get(OrderConstants.ID).asText();
        final String product = root.get(OrderConstants.PRODUCT).asText();
        final String client = root.get(OrderConstants.CLIENT).asText();
        final Instant placingDate = DateHelper.toInstant(root.get(OrderConstants.PLACING_DATE).asText());
        final Instant estimatedArrival = DateHelper.toInstant(root.get(OrderConstants.ESTIMATED_ARRIVAL).asText());
        switch (currentState) {
            case OrderConstants.PLACED_ORDER_STATE:
                return new PlacedOrder(id, product, client, placingDate, estimatedArrival);
            case OrderConstants.FAILED_ORDER_STATE:
                return new FailedOrder(id, product, client, placingDate, estimatedArrival);
            case OrderConstants.DELIVERED_ORDER_STATE:
                return new DeliveredOrder(id, product, client, placingDate, estimatedArrival);
            case OrderConstants.DELIVERING_ORDER_STATE:
                return new DeliveringOrder(id, product, client, placingDate, estimatedArrival);
            case OrderConstants.RESCHEDULED_ORDER_STATE:
                final Instant newEstimatedArrival = DateHelper.toInstant(
                        root.get(OrderConstants.NEW_ESTIMATED_ARRIVAL).asText()
                );
                return new RescheduledOrder(id, product, client, placingDate, estimatedArrival,
                        newEstimatedArrival);
            default:
                return null;
        }
    }
}
