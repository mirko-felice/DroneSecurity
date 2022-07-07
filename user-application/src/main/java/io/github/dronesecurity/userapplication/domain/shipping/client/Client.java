/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.client;

import io.github.dronesecurity.lib.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Entity} representing the client.
 */
public interface Client extends Entity<Client> {

    /**
     * Returns the name of the client.
     * @return his name
     */
    String name();

    /**
     * Mocks the client.
     * @return a new {@link Client}
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull Client mock() {
        return mock("John");
    }

    /**
     * Mocks the client using his name.
     * @param name name of the client
     * @return a new {@link Client}
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull Client mock(final String name) {
        return new MockClient(name);
    }

    /**
     * Mock client just for domain representation.
     */
    final class MockClient implements Client {
        private final String name;

        private MockClient(final String name) {
            this.name = name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String name() {
            return this.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasSameIdentityAs(final @NotNull Client entity) {
            return this.name.equals(entity.name());
        }
    }
}
