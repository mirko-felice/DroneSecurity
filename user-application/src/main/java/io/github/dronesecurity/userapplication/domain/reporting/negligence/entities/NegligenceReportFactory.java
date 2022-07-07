/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities;

import io.github.dronesecurity.userapplication.domain.user.entities.impl.CourierImpl;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.MaintainerImpl;
import io.github.dronesecurity.userapplication.common.data.entities.DroneData;
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
     * @param negligent the {@link CourierImpl} that committed the
     *                  negligence
     * @param assignee the {@link MaintainerImpl} assigned to the
     *                 report
     * @param data the {@link DroneData} associated to the report
     * @param orderId the order identifier related to the negligence
     * @param negligenceInstant {@link Instant} when the negligence has happened
     * @return a new {@link NegligenceReport}
     */
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static @NotNull NegligenceReport withoutID(final String negligent,
                                                      final String assignee,
                                                      final DroneData data,
                                                      final long orderId,
                                                      final Instant negligenceInstant) {
        return new NegligenceReportImpl(negligent, assignee, data, orderId, negligenceInstant);
    }

    /**
     * Creates a {@link ClosedNegligenceReport}.
     * @param report the {@link NegligenceReportWithID} to retrieve information from
     * @param closingInstant {@link Instant} when the report has been closed
     * @param solution {@link NegligenceSolution} used to take action
     * @return a new {@link ClosedNegligenceReport}
     */
    @Contract("_, _, _ -> new")
    public static @NotNull ClosedNegligenceReport closed(final @NotNull NegligenceReportWithID report,
                                                         final Instant closingInstant,
                                                         final NegligenceSolution solution) {
        return new ClosedNegligenceReportImpl(report, closingInstant, solution);
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
