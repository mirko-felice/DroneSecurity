/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.services;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;

/**
 * Domain Service dedicated to report negligence used in association with the drone.
 */
public interface DroneReporter {

    /**
     * Reports a negligence.
     * @param negligent {@link Negligent} committer
     * @param assignee {@link Assignee} of the report
     * @param data {@link DroneData} retrieved in that instant
     */
    void reportsNegligence(Negligent negligent, Assignee assignee, DroneData data);
}
