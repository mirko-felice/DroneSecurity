/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib.shared;

/**
 * Enumeration representing different levels of drone dangerous situations.
 */
public enum AlertLevel {
    /**
     * Stable situation.
     */
    STABLE,
    /**
     * Warning situation.
     */
    WARNING,
    /**
     * Critical situation.
     */
    CRITICAL
}
