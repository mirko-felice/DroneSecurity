/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities;

import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Helper for Date stuff.
 */
public final class DateHelper {

    private static final String ORDER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(ORDER_DATE_FORMAT, Locale.ITALY).withZone(ZONE_ID);

    private DateHelper() { }

    /**
     * Parse instant string to {@link Instant}.
     * @param instantString string to parse
     * @return the {@link Instant}
     */
    public static Instant toInstant(final String instantString) {
        return LocalDateTime.parse(instantString, FORMATTER).atZone(ZONE_ID).toInstant();
    }

    /**
     * Format an {@link Instant} to {@link String}.
     * @param instant instant to format
     * @return the {@link String}
     */
    public static @NotNull String toString(final Instant instant) {
        return FORMATTER.format(instant);
    }

    /**
     * Parse local date to {@link Instant}.
     * @param localDate the {@link LocalDate} to parse
     * @return the {@link Instant}
     */
    public static Instant fromLocalDate(final @NotNull LocalDate localDate) {
        return localDate.atTime(LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC);
    }
}
