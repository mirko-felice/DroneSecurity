/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts;

/**
 * Represents the possible states of the {@link Order}.
 */
public enum OrderState {

    /**
     * Represents the state in which the {@link Order} is only placed.
     */
    PLACED,

    /**
     * Represents the state in which the {@link Order} is being delivered.
     */
    DELIVERING,

    /**
     * Represents the state in which the {@link Order} has been successfully delivered.
     */
    SUCCEEDED,

    /**
     * Represents the state in which the {@link Order} has failed the delivery.
     */
    FAILED,

    /**
     * Represents the state in which the {@link Order} has been rescheduled.
     */
    RESCHEDULED
}
