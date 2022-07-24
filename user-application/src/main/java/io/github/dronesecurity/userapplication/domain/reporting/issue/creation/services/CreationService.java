/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.creation.services;

import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.SendingIssue;

/**
 * Service that allows the creation of a new issue.
 */
public interface CreationService {

    /**
     * Creates a new issue.
     * @param issue the issue to send
     */
    void addIssueReport(SendingIssue issue);
}
