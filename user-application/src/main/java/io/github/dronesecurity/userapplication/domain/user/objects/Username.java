/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.objects;

import io.github.dronesecurity.lib.ValueObject;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.exceptions.UsernameWithNumbersException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ValueObject} representing {@link User} identifier.
 */
public final class Username implements ValueObject<Username> {

    private final String value;

    private Username(final String value) {
        this.validate(value);
        this.value = value;
    }

    /**
     * Parse the string to build this value object.
     * @param value {@link String} to parse
     * @return the {@link Username}
     * @throws UsernameWithNumbersException if {@code value} contains at least one digit
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Username parse(final String value) {
        return new Username(value);
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public boolean isSameValueAs(final @NotNull Username username) {
        return this.value.equals(username.value);
    }

    /**
     * Gets the value object as a {@link String}.
     * @return the {@link String} representation
     */
    public String asString() {
        return this.value;
    }

    @Contract(pure = true)
    private void validate(final @NotNull String username) {
        if (username.matches("\\d+"))
            throw new UsernameWithNumbersException();
    }
}
