/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import java.nio.charset.StandardCharsets;

/**
 * Item representing a real camera sensor and observing its values.
 */
public class Camera extends AbstractSensor<Double> {

    private double distance;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return this.isRaspberry() ? "camera" : "cameraSimulator";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readData() {
        if (getOutputStream().size() > 0) {
            final String[] values = getOutputStream().toString(StandardCharsets.UTF_8).trim().split("\n");
            this.distance = Double.parseDouble(values[values.length - 1]);
            getOutputStream().reset();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getData() {
        return this.distance;
    }
}
