/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.objects;

import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.lib.ValueObject;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * {@link ValueObject} representing every possible {@link Order} date.
 */
public final class OrderDate implements ValueObject<OrderDate> {

    private static final Instant TODAY_INSTANT = truncate(Instant.now());

    /**
     * {@link OrderDate} representing today.
     */
    public static final OrderDate TODAY = OrderDate.parseInstant(TODAY_INSTANT);

    /**
     * {@link OrderDate} representing tomorrow.
     */
    public static final OrderDate TOMORROW = OrderDate.parseInstant(TODAY_INSTANT.plus(1, ChronoUnit.DAYS));
    private final Instant instant;

    /**
     * Build the value object using an {@link Instant}.
     * @param instant {@link Instant} to parse to create the value object
     */
    private OrderDate(final Instant instant) {
        this.instant = truncate(instant);
    }

    /**
     * Creates a new {@link OrderDate} using a {@link String}.
     * @param string {@link String} to parse to create the value object
     * @return a new fresh {@link OrderDate}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull OrderDate parseString(final String string) {
        return new OrderDate(DateHelper.toInstant(string));
    }

    /**
     * Creates a new {@link OrderDate} using a {@link Instant}.
     * @param instant {@link Instant} to parse to create the value object
     * @return a new fresh {@link OrderDate}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull OrderDate parseInstant(final Instant instant) {
        return new OrderDate(instant);
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public boolean isSameValueAs(final @NotNull OrderDate value) {
        return this.instant.equals(value.instant);
    }

    /**
     * Checks if this date is before the specified date.
     * @param value value to compare
     * @return true if this date is before the other
     */
    @Contract(pure = true)
    public boolean isBefore(final @NotNull OrderDate value) {
        return this.instant.isBefore(value.instant);
    }

    /**
     * Checks if this date is after the specified date.
     * @param value value to compare
     * @return true if this date is after the other
     */
    @Contract(pure = true)
    public boolean isAfter(final @NotNull OrderDate value) {
        return this.instant.isAfter(value.instant);
    }

    /**
     * Gets the value object as {@link String}.
     * @return the {@link String} value
     */
    public @NotNull String asString() {
        return DateHelper.toString(this.instant);
    }

    private static Instant truncate(final @NotNull Instant value) {
        return Instant.from(value.atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS));
    }
}
