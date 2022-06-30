/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib;

/**
 * Represents every entity of the domain.
 * @param <E> type parameter of the entity
 */
public interface Entity<E> {

    /**
     * Checks this entity has same identity as another entity of same type.
     * NOTE: entities have same identity only if they have same identifier, regardless of other attributes.
     * @param entity entity to compare
     * @return true if entities have the same identity, false otherwise
     */
    boolean hasSameIdentityAs(E entity);
}
