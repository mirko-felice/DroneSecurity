/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.dronesecurity.userapplication.shipping.courier.entities.Order;
import io.github.dronesecurity.userapplication.shipping.courier.entities.RescheduledOrder;
import io.github.dronesecurity.userapplication.shipping.courier.utilities.OrderConstants;
import io.github.dronesecurity.userapplication.utilities.DateHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Serialize {@link Order} into the corresponding Json.
 */
public final class OrderSerializer extends JsonSerializer<Order> {

    @Override
    public void serialize(final @NotNull Order value, final @NotNull JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField(OrderConstants.ID, value.getId());
        gen.writeStringField(OrderConstants.PRODUCT, value.getProduct());
        gen.writeStringField(OrderConstants.CLIENT, value.getClient());
        gen.writeStringField(OrderConstants.PLACING_DATE, DateHelper.toString(value.getPlacingDate()));
        gen.writeStringField(OrderConstants.ESTIMATED_ARRIVAL, DateHelper.toString(value.getEstimatedArrival()));
        if (value instanceof RescheduledOrder)
            gen.writeStringField(OrderConstants.NEW_ESTIMATED_ARRIVAL,
                    DateHelper.toString(((RescheduledOrder) value).getNewEstimatedArrival()));
        gen.writeArrayFieldStart(OrderConstants.EVENTS);
        gen.writeString(value.getCurrentState());
        gen.writeEndArray();
        gen.writeEndObject();
        gen.flush();
    }
}
