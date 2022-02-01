package it.unibo.dronesecurity.userapplication.events;

import java.util.*;
import java.util.function.Consumer;

/**
 * The class the management of domain events of a particular type.
 *
 * @param <T> the type of the event that will be managed by the instance
 */
public class DomainEvents<T extends Event> {

    private final transient List<Consumer<T>> consumers;

    /**
     * Builds the Domain Events.
     */
    public DomainEvents() {
        this.consumers = new ArrayList<>();
    }

    /**
     * Adds a handler to manage the arrival of an event.
     *
     * @param handler the handler to execute when an event is raised
     */
    public void register(final Consumer<T> handler) {
        this.consumers.add(handler);
    }

    /**
     * Raises the event for the domain.
     *
     * @param event the new event
     */
    public void raise(final T event) {
        this.consumers.forEach(cons -> cons.accept(event));
    }
}
