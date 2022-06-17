/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.userapplication.exceptions.ReportEmptyDataException;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.*;
import io.github.dronesecurity.userapplication.reporting.negligence.utilities.NegligenceConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link NegligenceReport} and its subclasses.
 */
final class ReportTest {

    private static final String NEGLIGENT = "courier";
    private static final String ASSIGNEE = "maintainer";
    private static final long ORDER_ID = 123;
    private static final NegligenceSolution SOLUTION = new NegligenceSolutionImpl("test Solution");

    @Test
    void testEmptyData() {
        final DroneData emptyData = this.generateReportWithoutData().getData();
        assertTrue(emptyData.isEmpty(), "Drone data should be empty.");
        assertNull(emptyData.getProximity(), "Report should have empty proximity value.");
        assertTrue(emptyData.getAccelerometer().isEmpty(), "Report should have empty accelerometer data.");
        assertNull(emptyData.getCamera(), "Report should have empty camera data.");
    }

    @Test
    void testFullData() {
        final NegligenceReport withData = this.generateReportWithData();
        assertFalse(withData.getData().isEmpty(), "Drone data should not be empty");
        assertNotNull(withData.getData().getProximity(), "Proximity should not be NULL.");
        assertFalse(withData.getData().getAccelerometer().isEmpty(), "Accelerometer data should not be empty.");
        assertNotNull(withData.getData().getCamera(), "Camera data should not be NULL.");
        assertNotEquals(withData.getData(), withData.getData().deepCopy(),
                "Data copy should be different of original.");
    }

    @Test
    void testOpenReport() {
        final OpenNegligenceReport openWithoutData = NegligenceReportFactory.open(
                NegligenceReportFactory.withID(0, this.generateReportWithoutData())
        );
        assertInstanceOf(OpenNegligenceReport.class, openWithoutData,
                "Object should be instance of " + OpenNegligenceReport.class + ".");
        assertThrowsExactly(ReportEmptyDataException.class, () -> openWithoutData.close(Instant.now(), SOLUTION),
                "Report can not be closed without providing data.");

        final OpenNegligenceReport openWithData = NegligenceReportFactory.open(
                NegligenceReportFactory.withID(0, this.generateReportWithData())
        );
        assertDoesNotThrow(() -> openWithData.close(Instant.now(), SOLUTION),
                "Closing report with data should not throw " + ReportEmptyDataException.class + ".");
    }

    @Test
    void testClosedReport() {
        final ClosedNegligenceReport report = NegligenceReportFactory.closed(
                NegligenceReportFactory.withID(0, this.generateReportWithData()), Instant.now(), SOLUTION
        );
        assertInstanceOf(ClosedNegligenceReport.class, report,
                "Object should be instance of " + ClosedNegligenceReport.class + ".");
        assertNotNull(report.getClosingInstant(),
                "Closed report should have its closing instant.");
    }

    @Contract(" -> new")
    private @NotNull NegligenceReport generateReportWithoutData() {
        return NegligenceReportFactory.withoutID(NEGLIGENT, ASSIGNEE, new DroneDataImpl(), ORDER_ID, Instant.now());
    }

    private @NotNull NegligenceReport generateReportWithData() {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode data = mapper.createObjectNode()
                .put(NegligenceConstants.PROXIMITY, 1)
                .put(NegligenceConstants.CAMERA, 1)
                .set(NegligenceConstants.ACCELEROMETER, mapper.createObjectNode()
                        .put(MqttMessageParameterConstants.ROLL, 1)
                        .put(MqttMessageParameterConstants.PITCH, 1)
                        .put(MqttMessageParameterConstants.YAW, 1));
        return NegligenceReportFactory.withoutID(NEGLIGENT, ASSIGNEE, new DroneDataImpl(data), ORDER_ID, Instant.now());
    }
}
