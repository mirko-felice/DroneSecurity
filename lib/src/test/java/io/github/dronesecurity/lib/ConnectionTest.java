/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link Connection}.
 */
final class ConnectionTest {

    @Test
    void cannotConnectWithoutCertificateFiles() {
        assertTrue(Connection.getInstance().connect().isCompletedExceptionally(),
                "Connection should not be created without certificate files.");
    }
}
