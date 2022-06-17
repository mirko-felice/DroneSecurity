/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.exceptions;

import io.github.dronesecurity.userapplication.shipping.entities.Order;

/**
 * Exception thrown if {@link Order} is built using an empty client.
 */
public final class EmptyClientException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public EmptyClientException() {
        super("Client MUST NOT be null or empty!");
    }
}
