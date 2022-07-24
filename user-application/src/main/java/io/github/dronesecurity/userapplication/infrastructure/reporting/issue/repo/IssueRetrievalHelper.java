/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.GenericUser;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.UserRole;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import org.jetbrains.annotations.NotNull;

/**
 * Repository helper that initializes a json body with the data of the logged user.
 */
public final class IssueRetrievalHelper {

    private IssueRetrievalHelper() { }

    /**
     * Initializes the query with user's data.
     * @return JsonObject containing the data of the logged user
     */
    public static @NotNull JsonObject initQueryWithUserData() {
        final JsonObject queryWithUser = new JsonObject();
        UserAPIHelper.get(UserAPIHelper.Operation.CHECK_LOGGED_USER_ROLE, BodyCodec.json(UserRole.class))
                .onSuccess(res -> {
                    final Future<GenericUser> loggedUser = getLoggedUserData(res.body());
                    loggedUser.onSuccess(user -> {
                        if (user.getRole() == UserRole.COURIER)
                            queryWithUser.put(IssueStringHelper.COURIER, user.getUsername());
                        else if (user.getRole() == UserRole.MAINTAINER)
                            queryWithUser.put(IssueStringHelper.ASSIGNEE, user.getUsername());
                    });
                });

        return queryWithUser;
    }

    private static Future<GenericUser> getLoggedUserData(final @NotNull UserRole userRole) {
        final Future<GenericUser> loggedUser;
        switch (userRole) {
            case COURIER:
                loggedUser = UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT,
                        BodyCodec.json(GenericUser.class)).map(HttpResponse::body);
                break;
            case MAINTAINER:
                loggedUser = UserAPIHelper.get(
                        UserAPIHelper.Operation.RETRIEVE_LOGGED_MAINTAINER_IF_PRESENT,
                        BodyCodec.json(GenericUser.class)).map(HttpResponse::body);
                break;
            default:
                loggedUser = Future.failedFuture(
                        new IllegalStateException("Unexpected value: " + userRole));
        }
        return loggedUser;
    }
}
