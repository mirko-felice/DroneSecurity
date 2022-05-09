package it.unibo.dronesecurity.userapplication.negligence.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.userapplication.exceptions.ReportEmptyDataException;
import it.unibo.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link NegligenceReport} and its subclasses.
 */
final class ReportTest {

    @Test
    void testBaseReportIsOpen() {
        final NegligenceReport report = this.initBuilder().build();
        assertInstanceOf(OpenNegligenceReport.class, report,
                "Object should be instance of " + OpenNegligenceReport.class + ".");
    }

    @Test
    void testBaseReportDataAreEmpty() {
        final NegligenceReport report = this.initBuilder().build();
        assertTrue(report.getData().isEmpty(), "Not providing data should create empty object.");
    }

    @Test
    void testReportCanNotBeClosedWithoutProvidingData() {
        assertThrowsExactly(ReportEmptyDataException.class, () -> this.initBuilder().closed(Instant.now()).build(),
                "Report can not be closed without providing data.");
    }

    @Test
    void testClosedReport() {
        final NegligenceReport report = new BaseNegligenceReport.Builder("courier", "maintainer",
                new DroneData(new ObjectMapper().createObjectNode().put(NegligenceConstants.PROXIMITY, 1.0)))
                .closed(Instant.now())
                .build();
        assertInstanceOf(ClosedNegligenceReport.class, report,
                "Object should be instance of " + ClosedNegligenceReport.class + ".");
    }

    @Contract(" -> new")
    private BaseNegligenceReport.@NotNull Builder initBuilder() {
        return new BaseNegligenceReport.Builder("courier", "maintainer", new DroneData());
    }
}
