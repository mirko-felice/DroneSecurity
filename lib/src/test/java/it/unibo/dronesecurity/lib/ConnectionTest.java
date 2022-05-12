package it.unibo.dronesecurity.lib;

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
