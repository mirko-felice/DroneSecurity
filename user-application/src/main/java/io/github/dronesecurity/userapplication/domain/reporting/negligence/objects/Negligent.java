/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.objects;

import io.github.dronesecurity.lib.shared.ValueObject;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions.EmptyNegligentException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * {@link ValueObject} representing the negligent related to a {@link NegligenceReport}.
 */
public final class Negligent implements ValueObject<Negligent> {

    private final String negligentUsername;

    private Negligent(final String negligentUsername) {
        this.validate(negligentUsername);
        this.negligentUsername = negligentUsername;
    }

    /**
     * Parses a string into the negligent.
     * @param value {@link String} to parse
     * @return a new {@link Assignee}
     * @throws EmptyNegligentException if {@code value} is null or empty
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Negligent parse(final String value) {
        return new Negligent(value);
    }

    /**
     * Gets the negligent as a {@link String}.
     * @return the string
     */
    public String asString() {
        return this.negligentUsername;
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public boolean isSameValueAs(final @NotNull Negligent value) {
        return this.negligentUsername.equals(value.negligentUsername);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.negligentUsername;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Negligent negligent = (Negligent) o;
        return this.negligentUsername.equals(negligent.negligentUsername);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.negligentUsername);
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) throw new EmptyNegligentException();
    }
}
