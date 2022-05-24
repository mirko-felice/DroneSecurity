/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package it.unibo.dronesecurity.lib;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link Connection}.
 */
final class ConnectionTest {

    private static final String TEST_TOPIC = "Test Topic";
    private static final String TEST_PARAMETER = "message";
    private static final String TEST_MESSAGE = "Test Message";

    @Test
    void cannotConnectWithoutCertificateFiles() {
        if (!Files.exists(Paths.get(System.getProperty("user.dir") + File.separator + "project.properties"))) {
            assertTrue(Connection.getInstance().connect().isCompletedExceptionally(),
                    "Connection should not be created without certificate files.");
        }
    }

    @Test
    void testSubscribeOnCI() {
        if (Files.exists(Paths.get(System.getProperty("user.dir") + File.separator + "project.properties"))) {
            final Connection connection = Connection.getInstance();
            assertFalse(connection.connect().isCompletedExceptionally(),
                    "Connection should not fail if certificates are set.");

            connection.subscribe(TEST_TOPIC, this::messageConsumer);
            connection.publish(TEST_TOPIC, new ObjectMapper().createObjectNode()
                    .put(TEST_PARAMETER, TEST_MESSAGE));

            connection.closeConnection();
        }
    }

    private void messageConsumer(final @NotNull MqttMessage message) {
        assertEquals(TEST_MESSAGE, Arrays.toString(message.getPayload()),
                "Message received should be equal to the message sent.");
    }
}
