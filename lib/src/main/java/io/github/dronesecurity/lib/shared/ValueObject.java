/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib.shared;

/**
 * Represents every value object of the domain.
 * @param <V> type parameter of the value object
 */
public interface ValueObject<V> {

    /**
     * Checks that this value object is same value as another value object of same type.
     * NOTE: value objects are the same value only if all own attributes are the same.
     * @param value value object to compare
     * @return true if value objects are the same value, false otherwise
     */
    boolean isSameValueAs(V value);
}
