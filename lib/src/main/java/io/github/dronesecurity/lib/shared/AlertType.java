/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib.shared;

/**
 * Enumeration representing different types of alert that can occur.
 */
public enum AlertType {

    /**
     * Alert caused by critical distance.
     */
    DISTANCE,

    /**
     * Alert caused by critical angle.
     */
    ANGLE,

    /**
     * Alert caused by critical camera situation.
     */
    CAMERA

}
