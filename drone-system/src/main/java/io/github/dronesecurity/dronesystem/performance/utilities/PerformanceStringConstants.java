/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.utilities;

/**
 * AWS Json Message performance parameters.
 */
public final class PerformanceStringConstants {

    /**
     * Parameter that contains image size of the last frame.
     */
    public static final String IMAGE_SIZE = "imageSize";

    /**
     * Parameter that contains timestamp of a data message.
     */
    public static final String TIMESTAMP = "timestamp";

    /**
     * Parameter that contains index of a data message.
     */
    public static final String INDEX = "index";

    /**
     * Parameter that contains the distance captured by the proximity sensor.
     */
    public static final String DISTANCE_PARAMETER = "distance";

    private PerformanceStringConstants() { }
}
