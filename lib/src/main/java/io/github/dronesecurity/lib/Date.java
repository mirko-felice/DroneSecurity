/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * {@link ValueObject} representing a simple instant of time as wanted.
 */
// TODO think about the name
public final class Date implements ValueObject<Date> {

    private final Instant instant;

    private Date(final Instant instant) {
        this.instant = instant;
    }

    /**
     * Creates a new {@link Date} using a {@link String}.
     * @param string {@link String} to parse to create the value object
     * @return a new fresh {@link Date}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Date parseString(final String string) {
        return new Date(DateHelper.toInstant(string));
    }

    /**
     * Creates a new {@link Date} using a {@link Instant}.
     * @param instant {@link Instant} to parse to create the value object
     * @return a new fresh {@link Date}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Date parseInstant(final Instant instant) {
        return new Date(instant);
    }

    /**
     * Creates a new {@link Date} for now.
     * @return a new fresh {@link Date}
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull Date now() {
        return new Date(Instant.now());
    }

    /**
     * Gets the value object as {@link String}.
     * @return the {@link String} value
     */
    public @NotNull String asString() {
        return DateHelper.toString(this.instant);
    }

    /**
     * Checks if this date is before the specified date.
     * @param value value to compare
     * @return true if this date is before the other
     */
    @Contract(pure = true)
    public boolean isBefore(final @NotNull Date value) {
        return this.instant.isBefore(value.instant);
    }

    /**
     * Checks if this date is after the specified date.
     * @param value value to compare
     * @return true if this date is after the other
     */
    @Contract(pure = true)
    public boolean isAfter(final @NotNull Date value) {
        return this.instant.isAfter(value.instant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final @NotNull Date value) {
        return this.instant.equals(value.instant);
    }
}
