/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.shipping.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.RescheduledOrder;
import io.github.dronesecurity.userapplication.infrastructure.shipping.OrderConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link Order} into the corresponding Json.
 */
public final class OrderSerializer extends JsonSerializer<Order> {

    @Override
    public void serialize(final @NotNull Order value,
                          final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(OrderConstants.ID, value.getId().asLong());
        gen.writeStringField(OrderConstants.PRODUCT, value.getProduct().name());
        gen.writeStringField(OrderConstants.CLIENT, value.getClient().name());
        gen.writeStringField(OrderConstants.PLACING_DATE, value.getPlacingDate().asString());
        gen.writeStringField(OrderConstants.ESTIMATED_ARRIVAL, value.getEstimatedArrival().asString());
        if (value instanceof RescheduledOrder)
            gen.writeStringField(OrderConstants.NEW_ESTIMATED_ARRIVAL,
                    ((RescheduledOrder) value).getNewEstimatedArrival().asString());
        gen.writeArrayFieldStart(OrderConstants.EVENTS);
        gen.writeString(value.getCurrentState().toString());
        gen.writeEndArray();
        gen.writeEndObject();
        gen.flush();
    }
}
