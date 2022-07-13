/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.objects;

import io.github.dronesecurity.lib.ValueObject;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions.NegligenceIdentifierCannotHaveNegativeValueException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ValueObject} representing the {@link NegligenceReport} unique identifier.
 */
public final class NegligenceIdentifier implements ValueObject<NegligenceIdentifier> {

    private static final long FIRST_ID = 1L;
    private static final NegligenceIdentifier FIRST_IDENTIFIER = new NegligenceIdentifier(FIRST_ID);
    private final long id;

    private NegligenceIdentifier(final long id) {
        this.validate(id);
        this.id = id;
    }

    /**
     * Retrieves the {@link NegligenceIdentifier} related to the first {@link NegligenceReport}.
     * @return the first {@link NegligenceIdentifier}
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull NegligenceIdentifier first() {
        return FIRST_IDENTIFIER;
    }

    /**
     * Creates a new {@link NegligenceIdentifier} using a long value.
     * @param value value used to build the {@link NegligenceIdentifier}
     * @return a new {@link NegligenceIdentifier}
     * @throws NegligenceIdentifierCannotHaveNegativeValueException if {@code value} is negative
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull NegligenceIdentifier fromLong(final long value) {
        return new NegligenceIdentifier(value);
    }

    /**
     * Gets the identifier as long.
     * @return the long value
     */
    public long asLong() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final @NotNull NegligenceIdentifier value) {
        return this.id == value.id;
    }

    private void validate(final long value) {
        if (value < FIRST_ID) throw new NegligenceIdentifierCannotHaveNegativeValueException();
    }
}
