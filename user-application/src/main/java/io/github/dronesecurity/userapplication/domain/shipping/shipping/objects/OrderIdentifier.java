/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.objects;

import io.github.dronesecurity.lib.ValueObject;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.OrderIdentifierCannotHaveNegativeValueException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ValueObject} representing the {@link Order} identifier.
 */
public final class OrderIdentifier implements ValueObject<OrderIdentifier> {

    private static final long FIRST_ID = 1L;
    private static final OrderIdentifier FIRST_IDENTIFIER = new OrderIdentifier(FIRST_ID);
    private final long id;

    private OrderIdentifier(final long id) {
        this.validate(id);
        this.id = id;
    }

    /**
     * Retrieves the {@link OrderIdentifier} related to the first {@link Order}.
     * @return the first {@link OrderIdentifier}
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull OrderIdentifier first() {
        return FIRST_IDENTIFIER;
    }

    /**
     * Creates a new {@link OrderIdentifier} using a long value.
     * @param value value used to build the {@link OrderIdentifier}
     * @return a new {@link OrderIdentifier}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull OrderIdentifier fromLong(final long value) {
        return new OrderIdentifier(value);
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public boolean isSameValueAs(final @NotNull OrderIdentifier value) {
        return this.id == value.id;
    }

    /**
     * Gets the value object as long.
     * @return the long value
     */
    public long asLong() {
        return this.id;
    }

    private void validate(final long value) {
        if (value < FIRST_ID) throw new OrderIdentifierCannotHaveNegativeValueException();
    }
}
