/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.events;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.events.DomainEvent;
import org.jetbrains.annotations.NotNull;

/**
 * {@link DomainEvent} raised when a courier commits negligence.
 */
public final class NewNegligence implements DomainEvent {

    private final NegligenceReport report;

    /**
     * Build the event using the {@link NegligenceReport}.
     * @param report report used to build the event
     */
    public NewNegligence(final @NotNull NegligenceReport report) {
        this.report = report;
    }

    /**
     * Gets the {@link NegligenceReport} representing the negligence committed by a {@link Negligent}.
     * @return the report
     */
    public NegligenceReport getReport() {
        return this.report;
    }

}
