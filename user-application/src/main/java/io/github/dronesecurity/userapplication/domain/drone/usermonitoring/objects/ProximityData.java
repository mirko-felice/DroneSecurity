/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects;

import io.github.dronesecurity.lib.CastHelper;
import io.github.dronesecurity.lib.Date;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

/**
 * Class representing a proximity data reception, containing its distance.
 */
public final class ProximityData extends SensorData {

    private final double distance;

    /**
     * Instantiates the Proximity data Read event.
     *
     * @param detectionInstant {@link Instant} when the detection is happened
     * @param orderId {@link Order} identifier related to data detection
     * @param distance data read by the proximity sensor
     */
    public ProximityData(final Date detectionInstant, final long orderId, final double distance) {
        super(detectionInstant, orderId);
        this.distance = distance;
    }

    /**
     * Gets proximity sensor data.
     *
     * @return value read by the proximity sensor
     */
    public double getDistance() {
        return this.distance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final @NotNull SensorData value) {
        final Optional<ProximityData> proximityData = CastHelper.safeCast(value, ProximityData.class);
        return proximityData
                .filter(data -> super.isSameValueAs(value)
                        && BigDecimal.valueOf(this.distance).compareTo(BigDecimal.valueOf(data.getDistance())) == 0)
                .isPresent();
    }
}
