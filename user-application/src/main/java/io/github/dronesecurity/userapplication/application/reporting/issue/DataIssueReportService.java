/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.Maintainer;
import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities.AbstractActiveIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.entities.OpenIssue;

import java.util.List;
import java.util.function.Consumer;

/**
 * Service dedicated to issue reporting.
 */
public interface DataIssueReportService {

    /**
     * Lists open issues reported.
     * @return the list of open issues reported
     */
    List<AbstractActiveIssue> getActiveIssueReports();

    /**
     * Lists issues reported that have been closed.
     * @return the list of closed issues
     */
    List<ClosedIssue> getClosedIssueReports();

    /**
     * Subscribes to new issues created by any courier.
     * @param maintainerUsername {@link Maintainer} username to track issue on
     * @param consumer consumes the {@link OpenIssue}
     */
    void subscribeToNewIssue(String maintainerUsername, Consumer<OpenIssue> consumer);
}
