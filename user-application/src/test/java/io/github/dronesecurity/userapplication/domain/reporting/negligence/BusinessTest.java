/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence;

import io.github.dronesecurity.userapplication.application.reporting.negligence.AssigneeReportsManagerImpl;
import io.github.dronesecurity.userapplication.application.reporting.negligence.DroneReporterImpl;
import io.github.dronesecurity.userapplication.application.reporting.negligence.NegligentReportsManagerImpl;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.repo.NegligenceRepository;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.services.AssigneeReportsManager;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.services.DroneReporter;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.services.NegligentReportsManager;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.repo.InMemoryNegligenceRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests business logic related to {@link NegligenceReport}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
final class BusinessTest {

    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private static final int THIRD = 3;
    private static final NegligenceRepository NEGLIGENCE_REPOSITORY = new InMemoryNegligenceRepository();
    private static final DroneReporter DRONE_REPORTER = new DroneReporterImpl(NEGLIGENCE_REPOSITORY);
    private static final AssigneeReportsManager ASSIGNEE_REPORTS_MANAGER =
            new AssigneeReportsManagerImpl(NEGLIGENCE_REPOSITORY);
    private static final NegligentReportsManager NEGLIGENT_REPORTS_MANAGER =
            new NegligentReportsManagerImpl(NEGLIGENCE_REPOSITORY);
    private static final Assignee ASSIGNEE = Assignee.parse("assignee");
    private static final Negligent NEGLIGENT = Negligent.parse("negligent");
    private static final DroneData DRONE_DATA = DroneData.generate(10.0, 0, 0, 0, 1000L);

    @Test
    @Order(FIRST)
    void testInitialValidation() {
        assertTrue(ASSIGNEE_REPORTS_MANAGER.retrieveOpenReportsForAssignee(ASSIGNEE).isEmpty(),
                "At first assignee should not have any open report.");
        assertTrue(ASSIGNEE_REPORTS_MANAGER.retrieveClosedReportsForAssignee(ASSIGNEE).isEmpty(),
                "At first assignee should not have any closed report.");

        assertTrue(NEGLIGENT_REPORTS_MANAGER.retrieveOpenReportsForNegligent(NEGLIGENT).isEmpty(),
                "At first negligent should not have any open report.");
        assertTrue(NEGLIGENT_REPORTS_MANAGER.retrieveClosedReportsForNegligent(NEGLIGENT).isEmpty(),
                "At first negligent should not have any closed report.");
    }

    @Test
    @Order(SECOND)
    void testReporting() {
        DRONE_REPORTER.reportsNegligence(NEGLIGENT, ASSIGNEE, DRONE_DATA);
        assertEquals(1, ASSIGNEE_REPORTS_MANAGER.retrieveOpenReportsForAssignee(ASSIGNEE).size(),
                "After reporting once, assignee should have exactly one open report.");
        assertEquals(1, NEGLIGENT_REPORTS_MANAGER.retrieveOpenReportsForNegligent(NEGLIGENT).size(),
                "After reporting once, negligent should have exactly one open report.");
    }

    @Test
    @Order(THIRD)
    void testTakeAction() {
        final OpenNegligenceReport report = ASSIGNEE_REPORTS_MANAGER.retrieveOpenReportsForAssignee(ASSIGNEE).get(0);
        final String solution = "solution";
        ASSIGNEE_REPORTS_MANAGER.takeAction(report, NegligenceActionForm.create(solution));
        assertEquals(1, ASSIGNEE_REPORTS_MANAGER.retrieveClosedReportsForAssignee(ASSIGNEE).size(),
                "After taking action, assignee should have exactly one closed report.");
        assertEquals(1, NEGLIGENT_REPORTS_MANAGER.retrieveClosedReportsForNegligent(NEGLIGENT).size(),
                "After taking action, negligent should have exactly one closed report.");
    }
}
