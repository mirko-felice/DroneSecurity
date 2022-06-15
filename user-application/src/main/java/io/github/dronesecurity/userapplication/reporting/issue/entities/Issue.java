/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.issue.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.reporting.issue.serialization.IssueDeserializer;
import io.github.dronesecurity.userapplication.reporting.issue.serialization.IssueSerializer;

import java.time.Instant;

/**
 * Interface of an issue report.
 */
@JsonDeserialize(using = IssueDeserializer.class)
@JsonSerialize(using = IssueSerializer.class)
public interface Issue {

    /**
     * Gets the subject of the issue, specified by the {@link Courier}.
     * @return the subject of this issue
     */
    String getSubject();

    /**
     * Gets the details of the issue.
     * @return the details related to this issue
     */
    String getDetails();

    /**
     * Gets the Courier that created the issue report.
     * @return the Courier that created the report
     */
    String getCourier();

    /**
     * Gets the {@link io.github.dronesecurity.userapplication.auth.entities.Maintainer} assigned to the issue.
     * @return the {@link io.github.dronesecurity.userapplication.auth.entities.Maintainer} username
     */
    String assignedTo();

    /**
     * Gets the timestamp of when the report was sent.
     * @return the creation timestamp
     */
    Instant getReportingDate();

    /**
     * Gets the string representation of the state of the object.
     * @return the string representation of the state
     */
    String getState();

    /**
     * Gets the drone identifier that owns the issue.
     * @return the drone identifier
     */
    String getDroneId();
}
