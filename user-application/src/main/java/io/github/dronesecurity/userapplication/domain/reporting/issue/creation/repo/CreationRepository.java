/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.creation.repo;

import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.SendingIssue;
import io.vertx.core.json.JsonObject;

/**
 * Repository that provides a way to add a new issue to the system.
 */
public interface CreationRepository {

    /**
     * Adds a new open issue.
     * @param issue the issue to add to repository
     * @return json representing the newly added issue
     */
    JsonObject addIssue(SendingIssue issue);
}
