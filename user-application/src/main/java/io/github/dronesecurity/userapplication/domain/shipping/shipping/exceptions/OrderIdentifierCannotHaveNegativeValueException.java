/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions;

import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;

/**
 * Exception thrown if {@link OrderIdentifier} is built using a negative value.
 */
public final class OrderIdentifierCannotHaveNegativeValueException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Build the exception.
     */
    public OrderIdentifierCannotHaveNegativeValueException() {
        super("Order identifier MUST NOT have negative value!");
    }
}
