/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

/**
 * Constants related to all the possible drone situations.
 */
public final class SituationConstants {

    /**
     * Represents the stable situation.
     */
    public static final String STABLE = "STABLE";

    /**
     * Represents the situation in which inclination angle is critical.
     */
    public static final String CRITICAL_ANGLE = "CRITICAL ANGLE";

    /**
     * Represents the situation in which proximity distance is critical.
     */
    public static final String CRITICAL_DISTANCE = "CRITICAL DISTANCE";

    /**
     * Represents the situation in which inclination angle is dangerous.
     */
    public static final String DANGEROUS_ANGLE = "DANGEROUS ANGLE";

    /**
     * Represents the situation in which proximity distance is dangerous.
     */
    public static final String DANGEROUS_DISTANCE = "DANGEROUS DISTANCE";

    private SituationConstants() { }
}
