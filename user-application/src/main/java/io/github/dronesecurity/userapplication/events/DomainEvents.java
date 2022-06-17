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

    private static final Map<Class<? extends Event>, List<Consumer<? extends Event>>> ALL_CONSUMERS =
            new ConcurrentHashMap<>();

    private DomainEvents() { }

    /**
     * Adds a handler to manage the arrival of an event.
     * @param clazz {@link Class} of the {@link Event} to register on
     * @param handler the {@link Consumer} to execute when an event is raised
     * @param <T> type parameter to constraint consuming only {@link Event} subclasses
     */
    public static <T extends Event> void register(final Class<T> clazz, final Consumer<T> handler) {
        ALL_CONSUMERS.computeIfAbsent(clazz, k -> new ArrayList<>());
        ALL_CONSUMERS.get(clazz).add(handler);
    }

    /**
     * Removes a handler previously registered.
     * @param clazz {@link Class} of the {@link Event} to unregister from
     * @param handler the {@link Consumer} previously registered
     * @param <T> type parameter to constraint consuming only {@link Event} subclasses
     */
    public static <T extends Event> void unregister(final Class<T> clazz, final Consumer<T> handler) {
        final List<Consumer<? extends Event>> consumers = ALL_CONSUMERS.get(clazz);
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
     * @param <T> type parameter to constraint raising only {@link Event} subclasses
     */
    @SuppressWarnings("unchecked")
    public static <T extends Event> void raise(final @NotNull T event) {
        for (final Consumer<? extends Event> cons : ALL_CONSUMERS.get(event.getClass())) {
            ((Consumer<T>) cons).accept(event);
        }
    }

}
