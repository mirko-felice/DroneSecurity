/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;

/**
 * Exception thrown if {@link Product} is built using an empty product name.
 */
public final class EmptyProductNameException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public EmptyProductNameException() {
        super("Product name MUST NOT be null or empty!");
    }
}
