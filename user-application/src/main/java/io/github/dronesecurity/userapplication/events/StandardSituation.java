/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

/**
 * The event to be raised when alert is no more occurring and standard situation comes back.
 */
public class StandardSituation implements Event {

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Alert is no more occurring, situation comes back to a standard form.";
    }
}