/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.entities;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Factory class to generate different type of {@link NegligenceReport}.
 */
public final class NegligenceReportFactory {

    private NegligenceReportFactory() { }

    /**
     * Creates a {@link NegligenceReport} without ID.
     * @param negligent the {@link Courier} that committed the
     *                  negligence
     * @param assignee the {@link Maintainer} assigned to the
     *                 report
     * @param data the {@link DroneData} associated to the report
     * @return a new {@link NegligenceReport}
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull NegligenceReport withoutID(final String negligent,
                                                      final String assignee,
                                                      final DroneData data) {
        return new NegligenceReportImpl(negligent, assignee, data);
    }

    /**
     * Creates a {@link ClosedNegligenceReport}.
     * @param report the {@link NegligenceReportWithID} to retrieve information from
     * @param closingInstant {@link Instant} when the report has been closed
     * @return a new {@link ClosedNegligenceReport}
     */
    @Contract("_, _ -> new")
    public static @NotNull ClosedNegligenceReport closed(final @NotNull NegligenceReportWithID report,
                                                         final Instant closingInstant) {
        return new ClosedNegligenceReportImpl(report, closingInstant);
    }

    /**
     * Creates a {@link OpenNegligenceReport}.
     * @param report the {@link NegligenceReportWithID} to retrieve information from
     * @return a new {@link OpenNegligenceReport}
     */
    @Contract("_ -> new")
    public static @NotNull OpenNegligenceReport open(final NegligenceReportWithID report) {
        return new OpenNegligenceReportImpl(report);
    }

    /**
     * Creates a generic {@link NegligenceReportWithID}.
     * @param id id of the report
     * @param report the {@link NegligenceReport} to retrieve information from
     * @return a new {@link NegligenceReportWithID}
     */
    @Contract("_, _ -> new")
    public static @NotNull NegligenceReportWithID withID(final long id, final NegligenceReport report) {
        return new NegligenceReportWithIDImpl(id, report);
    }
}
