/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

import io.github.dronesecurity.userapplication.domain.user.entities.impl.CourierImpl;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.NegligenceReport;
import org.jetbrains.annotations.NotNull;

/**
 * The event to be raised when a {@link CourierImpl} commits negligence.
 */
public class NewNegligence implements DomainEvent {

    private final NegligenceReport report;

    /**
     * Build the event using the {@link NegligenceReport}.
     * @param report report used to build the event
     */
    public NewNegligence(final @NotNull NegligenceReport report) {
        this.report = report;
    }

    /**
     * Gets the {@link NegligenceReport} representing the negligence committed by a {@link CourierImpl}.
     * @return the report
     */
    public NegligenceReport getReport() {
        return this.report;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "NewNegligence{ report = " + this.report + '}';
    }
}
