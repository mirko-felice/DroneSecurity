/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.objects;

import io.github.dronesecurity.lib.shared.Date;
import io.github.dronesecurity.lib.shared.ValueObject;
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
    private final Date instant;

    private OrderDate(final Instant instant) {
        this.instant = Date.parseInstant(truncate(instant));
    }

    /**
     * Creates a new {@link OrderDate} using a {@link String}.
     * @param string {@link String} to parse to create the value object
     * @return a new fresh {@link OrderDate}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull OrderDate parseString(final String string) {
        return new OrderDate(Date.parseString(string).asInstant());
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
        return this.instant.asString();
    }

    private static Instant truncate(final @NotNull Instant value) {
        return Instant.from(value.atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS));
    }
}
