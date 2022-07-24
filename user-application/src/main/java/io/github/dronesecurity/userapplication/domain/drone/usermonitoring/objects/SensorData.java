/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.lib.shared.Date;
import io.github.dronesecurity.lib.shared.ValueObject;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.serializers.SensorDataDeserializer;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.serializers.SensorDataSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Class representing a generic data reception, containing the instant of when the data is received and its order id.
 */
@JsonSerialize(using = SensorDataSerializer.class)
@JsonDeserialize(using = SensorDataDeserializer.class)
public class SensorData implements ValueObject<SensorData> {

    private final Date detectionInstant;
    private final long orderId;

    /**
     * Build the object starting from data.
     * @param detectionInstant {@link Date} of when the detection is happened
     * @param orderId order identifier related to data detection
     */
    protected SensorData(final Date detectionInstant, final long orderId) {
        this.detectionInstant = detectionInstant;
        this.orderId = orderId;
    }

    /**
     * Gets the instant of when the data was received.
     * @return The instant of when the data was received
     */
    public Date getDetectionInstant() {
        return this.detectionInstant;
    }

    /**
     * Gets the identifier of the order related to this data.
     * @return the order identifier related to the data
     */
    public long getOrderId() {
        return this.orderId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final @NotNull SensorData value) {
        return this.orderId == value.getOrderId() && this.detectionInstant.equals(value.getDetectionInstant());
    }
}
