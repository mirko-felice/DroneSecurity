/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Manager class of all domain events.
 */
public final class DomainEvents {

    private static final Map<Class<? extends DomainEvent>, List<Consumer<? extends DomainEvent>>> ALL_CONSUMERS =
            new ConcurrentHashMap<>();

    private DomainEvents() { }

    /**
     * Adds a handler to manage the arrival of an event.
     * @param clazz {@link Class} of the {@link DomainEvent} to register on
     * @param handler the {@link Consumer} to execute when an event is raised
     * @param <T> type parameter to constraint consuming only {@link DomainEvent} subclasses
     */
    public static <T extends DomainEvent> void register(final Class<T> clazz, final Consumer<T> handler) {
        ALL_CONSUMERS.computeIfAbsent(clazz, k -> new ArrayList<>());
        ALL_CONSUMERS.get(clazz).add(handler);
    }

    /**
     * Removes a handler previously registered.
     * @param clazz {@link Class} of the {@link DomainEvent} to unregister from
     * @param handler the {@link Consumer} previously registered
     * @param <T> type parameter to constraint consuming only {@link DomainEvent} subclasses
     */
    public static <T extends DomainEvent> void unregister(final Class<T> clazz, final Consumer<T> handler) {
        final List<Consumer<? extends DomainEvent>> consumers = ALL_CONSUMERS.get(clazz);
        if (consumers != null && handler != null) {
            consumers.remove(handler);
            if (consumers.isEmpty())
                ALL_CONSUMERS.remove(clazz);
        }
    }

    /**
     * Raises the event for the domain.
     *
     * @param event the new event
     * @param <T> type parameter to constraint raising only {@link DomainEvent} subclasses
     */
    @SuppressWarnings("unchecked")
    public static <T extends DomainEvent> void raise(final @NotNull T event) {
        final List<Consumer<? extends DomainEvent>> consumers = List.copyOf(ALL_CONSUMERS.get(event.getClass()));
        for (final Consumer<? extends DomainEvent> cons : consumers) {
            ((Consumer<T>) cons).accept(event);
        }
    }

}
