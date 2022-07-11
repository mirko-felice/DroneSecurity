/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.objects;

import io.github.dronesecurity.lib.ValueObject;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions.EmptyNegligentException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ValueObject} representing the negligent related to a {@link NegligenceReport}.
 */
public final class Negligent implements ValueObject<Negligent> {

    private final String username;

    private Negligent(final String username) {
        this.validate(username);
        this.username = username;
    }

    /**
     * Parses a string into the negligent.
     * @param value {@link String} to parse
     * @return a new {@link Assignee}
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
        return this.username;
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public boolean isSameValueAs(final @NotNull Negligent value) {
        return this.username.equals(value.username);
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) throw new EmptyNegligentException();
    }
}
