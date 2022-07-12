/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication;

import io.github.dronesecurity.userapplication.presentation.Launcher;

/**
 * Starter Jar class.
 */
public final class Starter {

    private Starter() { }

    /**
     * Main additional arguments.
     * @param args additional args
     */
    public static void main(final String[] args) {
        Launcher.main(args);
    }
}
