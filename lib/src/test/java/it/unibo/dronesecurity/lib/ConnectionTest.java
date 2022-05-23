package it.unibo.dronesecurity.lib;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link Connection}.
 */
final class ConnectionTest {

    @Test
    void cannotConnectWithoutCertificateFiles() {
        if (!Files.exists(Paths.get(System.getProperty("user.dir") + File.separator + "cert"))) {
            assertTrue(Connection.getInstance().connect().isCompletedExceptionally(),
                    "Connection should not be created without certificate files.");
        }
    }
}
