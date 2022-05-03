package it.unibo.dronesecurity.userapplication.negligence.entities;

import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.utilities.ReportEmptyDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link NegligenceReport} and its subclasses.
 */
final class ReportTest {

    private static final Maintainer MAINTAINER = Maintainer.complete("maintainer", "maintainerPassword");
    private static final Courier COURIER = Courier.complete("courier", "courierPassword", MAINTAINER);
    private BaseNegligenceReport.Builder builder;

    @BeforeEach
    void setup() {
        this.builder = new BaseNegligenceReport.Builder(COURIER, MAINTAINER);
    }

    @Test
    void testBaseReportIsOpen() {
        final NegligenceReport report = this.builder.build();
        assertInstanceOf(OpenNegligenceReport.class, report,
                "Object should be instance of " + OpenNegligenceReport.class + ".");
    }

    @Test
    void testBaseReportDataAreEmpty() {
        final NegligenceReport report = this.builder.build();
        assertTrue(report.getData().isEmpty(), "Not providing data should create empty object.");
    }

    @Test
    void testReportCanNotBeClosedWithoutProvidingData() {
        assertThrowsExactly(ReportEmptyDataException.class, () -> this.builder.closed(Instant.now()).build(),
                "Report can not be closed without providing data.");
    }

    @Test
    void testClosedReport() {
        final NegligenceReport report = this.builder
                .withProximity(1.0)
                .closed(Instant.now())
                .build();
        assertInstanceOf(ClosedNegligenceReport.class, report,
                "Object should be instance of " + ClosedNegligenceReport.class + ".");
    }

}
