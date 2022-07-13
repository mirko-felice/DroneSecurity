/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence;

import io.github.dronesecurity.lib.Date;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.impl.ClosedNegligenceReportImpl;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.impl.OpenNegligenceReportImpl;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions.*;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link NegligenceReport} aggregate.
 */
final class NegligenceAggregateTest {

    private static final long NEGATIVE_ID = -10L;
    private static final long SIMPLE_ID = 3L;
    private static final NegligenceIdentifier NEGLIGENCE_IDENTIFIER = NegligenceIdentifier.first();
    private static final Assignee ASSIGNEE = Assignee.parse("assignee");
    private static final Negligent NEGLIGENT = Negligent.parse("negligent");
    private static final DroneData DRONE_DATA = DroneData.generate(10.0, 0, 0, 0, 1000L);

    @Test
    void testNegligenceIdentifier() {
        assertThrowsExactly(NegligenceIdentifierCannotHaveNegativeValueException.class,
                () -> NegligenceIdentifier.fromLong(NEGATIVE_ID),
                "Negligence Identifier should not have negative value.");

        final NegligenceIdentifier first = NegligenceIdentifier.first();
        assertEquals(1L, first.asLong(), "First Negligence Identifier should be 1.");
        assertTrue(first.isSameValueAs(NegligenceIdentifier.first()),
                "Negligence Identifiers should be equals if both first.");

        final NegligenceIdentifier simpleIdentifier = NegligenceIdentifier.fromLong(SIMPLE_ID);
        assertTrue(simpleIdentifier.isSameValueAs(simpleIdentifier),
                "Negligence Identifiers should be equals if both built using same value.");

        assertFalse(first.isSameValueAs(simpleIdentifier),
                "Negligence Identifiers should be different if built using different values.");
    }

    @Test
    void testNegligent() {
        assertThrowsExactly(EmptyNegligentException.class,
                () -> Negligent.parse(null),
                "Negligent should not be null.");
        assertThrowsExactly(EmptyNegligentException.class,
                () -> Negligent.parse(""),
                "Negligent should not be empty.");

        final String username = "negligent";
        final Negligent negligent = Negligent.parse(username);
        assertEquals(username, negligent.asString(),
                "Negligent should be the same used to build the Value Object.");
        assertTrue(negligent.isSameValueAs(negligent),
                "Negligent should be equals if their name is the same.");

        final Negligent anotherNegligent = Negligent.parse("another");
        assertFalse(negligent.isSameValueAs(anotherNegligent),
                "Negligent should be different if their names are different.");
    }

    @Test
    void testAssignee() {
        assertThrowsExactly(EmptyAssigneeException.class,
                () -> Assignee.parse(null),
                "Assignee should not be null.");
        assertThrowsExactly(EmptyAssigneeException.class,
                () -> Assignee.parse(""),
                "Assignee should not be empty.");

        final String username = "assignee";
        final Assignee assignee = Assignee.parse(username);
        assertEquals(username, assignee.asString(),
                "Assignee should be the same used to build the Value Object.");
        assertTrue(assignee.isSameValueAs(assignee),
                "Assignee should be equals if their name is the same.");

        final Assignee anotherAssignee = Assignee.parse("another");
        assertFalse(assignee.isSameValueAs(anotherAssignee),
                "Assignee should be different if their names are different.");
    }

    @Test
    void testDroneData() {
        final double validProximity = 10.0;
        final int validAngle = 0;
        final long validImageSize = 1000L;

        final double invalidProximity = -10.0;
        assertThrowsExactly(InvalidDroneDataException.class,
                () -> DroneData.generate(invalidProximity, validAngle, validAngle, validAngle, validImageSize),
                "Proximity can not be negative.");

        final int invalidAngle = 200;
        assertThrowsExactly(InvalidDroneDataException.class,
                () -> DroneData.generate(validProximity, invalidAngle, invalidAngle, invalidAngle, validImageSize),
                "Every angle must be between -180\u00B0 and +180\u00B0.");

        final long invalidImageSize = -100L;
        assertThrowsExactly(InvalidDroneDataException.class,
                () -> DroneData.generate(validProximity, validAngle, validAngle, validAngle, invalidImageSize),
                "Image size can not be negative.");

        final DroneData droneData =
                DroneData.generate(validProximity, validAngle, validAngle, validAngle, validImageSize);
        assertEquals(validProximity, droneData.getProximity(),
                "Proximity should be the same used to build the Value Object.");
        assertTrue(droneData.isSameValueAs(droneData),
                "Drone Data should be equal to itself.");

        final DroneData anotherDroneData =
                DroneData.generate(validProximity, validAngle, validAngle, validAngle, validImageSize);
        assertFalse(droneData.isSameValueAs(anotherDroneData),
                "Drone data should be different even with same values because of different creation instant.");
    }

    @Test
    void testNegligenceActionForm() {
        assertThrowsExactly(EmptySolutionException.class,
                () -> NegligenceActionForm.create(null),
                "Solution should not be null.");
        assertThrowsExactly(EmptySolutionException.class,
                () -> NegligenceActionForm.create(""),
                "Solution should not be empty.");

        final String solution = "solution";
        final NegligenceActionForm actionForm = NegligenceActionForm.create(solution);
        assertEquals(solution, actionForm.getSolution(),
                "Solution should be the same used to build the Value Object.");
        assertTrue(actionForm.isSameValueAs(actionForm),
                "Action form should be equal to itself.");

        final NegligenceActionForm anotherForm = NegligenceActionForm.create(solution);
        assertFalse(actionForm.isSameValueAs(anotherForm),
                "Negligent should be different even with same solution because of different creation instant.");
    }

    @Test
    void testNegligenceReportValidation() {
        final Date yesterday = Date.parseInstant(Instant.now().minus(2, ChronoUnit.DAYS));
        assertThrowsExactly(InvalidClosingInstantException.class,
                () -> new ClosedNegligenceReportImpl(NEGLIGENCE_IDENTIFIER, NEGLIGENT, ASSIGNEE, DRONE_DATA,
                        NegligenceActionForm.parse("solution", yesterday)));
    }

    @Test
    void testNegligenceReportIdentity() {
        final NegligenceReport report =
                new OpenNegligenceReportImpl(NEGLIGENCE_IDENTIFIER, NEGLIGENT, ASSIGNEE, DRONE_DATA);
        assertTrue(report.hasSameIdentityAs(report), "Entity should have same identity as itself.");
        final NegligenceReport anotherReport =
                new OpenNegligenceReportImpl(NegligenceIdentifier.fromLong(2L), NEGLIGENT, ASSIGNEE, DRONE_DATA);
        assertFalse(report.hasSameIdentityAs(anotherReport),
                "Entities with same attributes but different identifiers should not have same identity.");
    }
}
