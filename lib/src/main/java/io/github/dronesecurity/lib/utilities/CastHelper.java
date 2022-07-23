/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib.utilities;

import java.util.Optional;

/**
 * Helper to safe casting.
 */
public final class CastHelper {

    private CastHelper() { }

    /**
     * Utility method to safe casting any class, possibly giving back an empty Optional.
     * @param candidate the candidate that should be cast
     * @param targetClass the class which the candidate should be cast on
     * @param <S> the type of given candidate
     * @param <T> the type of target class
     * @return the {@link Optional} of {@code targetClass}
     */
    public static <S, T> Optional<T> safeCast(final S candidate, final Class<T> targetClass) {
        return targetClass.isInstance(candidate)
                ? Optional.of(targetClass.cast(candidate))
                : Optional.empty();
    }
}
