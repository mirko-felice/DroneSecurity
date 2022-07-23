/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.objects;

import io.github.dronesecurity.lib.shared.ValueObject;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.EmptyProductNameException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ValueObject} representing the requested product in an {@link Order}.
 */
public final class Product implements ValueObject<Product> {

    private final String productName;

    private Product(final String name) {
        this.validate(name);
        this.productName = name;
    }

    /**
     * Creates a new {@link Product} using its name.
     * @param name product name used to build the {@link Product}
     * @return a new fresh {@link Product}
     * @throws EmptyProductNameException if {@code name} is null or empty
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Product fromName(final String name) {
        return new Product(name);
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public boolean isSameValueAs(final @NotNull Product value) {
        return this.productName.equals(value.productName);
    }

    /**
     * Gets the product name as {@link String}.
     * @return the product name
     */
    public String name() {
        return this.productName;
    }

    private void validate(final String value) {
        if (value == null || value.isEmpty()) throw new EmptyProductNameException();
    }
}
