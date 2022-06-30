/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.shipping.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.OrderState;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.impl.*;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import io.github.dronesecurity.userapplication.infrastructure.shipping.OrderConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

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
        final OrderState currentState = OrderState.valueOf(events.get(events.size() - 1).asText());
        final OrderIdentifier orderIdentifier = OrderIdentifier.fromLong(root.get(OrderConstants.ID).asLong());
        final Product product = Product.fromName(root.get(OrderConstants.PRODUCT).asText());
        final Client client = Client.mock(root.get(OrderConstants.CLIENT).asText());
        final OrderDate placingDate = OrderDate.parseString(root.get(OrderConstants.PLACING_DATE).asText());
        final OrderDate estimatedArrival =
                OrderDate.parseString(root.get(OrderConstants.ESTIMATED_ARRIVAL).asText());
        switch (currentState) {
            case PLACED:
                return new PlacedOrderImpl(orderIdentifier, product, client, placingDate, estimatedArrival);
            case DELIVERING:
                return new DeliveringOrderImpl(orderIdentifier, product, client, placingDate, estimatedArrival);
            case SUCCEEDED:
                return new SucceededOrderImpl(orderIdentifier, product, client, placingDate, estimatedArrival);
            case FAILED:
                return new FailedOrderImpl(orderIdentifier, product, client, placingDate, estimatedArrival);
            case RESCHEDULED:
                final OrderDate newEstimatedArrival = OrderDate.parseString(
                        root.get(OrderConstants.NEW_ESTIMATED_ARRIVAL).asText()
                );
                return new RescheduledOrderImpl(orderIdentifier, product, client, placingDate, estimatedArrival,
                        newEstimatedArrival);
            default:
                return null;
        }
    }
}
