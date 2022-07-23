/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.drone.usermonitoring.objects;

import io.github.dronesecurity.lib.utilities.CastHelper;
import io.github.dronesecurity.lib.shared.Date;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;

/**
 * Class representing a camera data reception, containing its image size.
 */
public final class CameraData extends SensorData {

    private final int imageSize;

    /**
     * Instantiates the Camera data Read event.
     *
     * @param detectionInstant {@link Instant} when the detection is happened
     * @param orderId {@link Order} identifier related to data detection
     * @param imageSize image size read by the camera
     */
    public CameraData(final Date detectionInstant, final long orderId, final int imageSize) {
        super(detectionInstant, orderId);
        this.imageSize = imageSize;
    }

    /**
     * Gets image size.
     *
     * @return value read by the camera
     */
    public int getImageSize() {
        return this.imageSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final @NotNull SensorData value) {
        final Optional<CameraData> cameraData = CastHelper.safeCast(value, CameraData.class);
        return cameraData
                .filter(data -> super.isSameValueAs(value) && this.imageSize == data.getImageSize()).isPresent();
    }
}
