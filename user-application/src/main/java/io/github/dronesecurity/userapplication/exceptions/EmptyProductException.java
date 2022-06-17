/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.exceptions;

import io.github.dronesecurity.userapplication.shipping.entities.Order;

/**
 * Exception thrown if {@link Order} is built using an empty product.
 */
public final class EmptyProductException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public EmptyProductException() {
        super("Product MUST NOT be null or empty!");
    }
}
