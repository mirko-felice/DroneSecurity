/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.UserRole;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.CourierImpl;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.MaintainerImpl;
import io.github.dronesecurity.lib.utilities.CastHelper;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.ext.web.codec.BodyCodec;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller dedicated to manage both {@link CourierImpl} and
 * {@link MaintainerImpl} actions.
 */
public class UserController implements Initializable {

    private static final double LOGIN_MIN_WIDTH = 350;
    private static final double LOGIN_MIN_HEIGHT = 200;
    private static final double ORDERS_MIN_WIDTH = 800;
    private static final double ORDERS_MIN_HEIGHT = 400;
    private static final double ISSUES_MIN_WIDTH = 550;
    private static final double ISSUES_MIN_HEIGHT = 400;
    private static final double NEGLIGENCE_MIN_WIDTH = 600;
    private static final double NEGLIGENCE_MIN_HEIGHT = 500;
    private static final double NEGLIGENCE_DATA_MIN_WIDTH = 650;
    private static final double NEGLIGENCE_DATA_MIN_HEIGHT = 300;
    private static final String LOGIN_FXML = "login.fxml";
    private static final String ORDERS_FXML = "shipping/orders.fxml";
    private static final String NEGLIGENCE_FXML = "reporting/negligence/assignee.fxml";
    private static final String NEGLIGENCE_DATA_FXML = "reporting/negligence/negligenceData.fxml";
    private static final String ISSUES_FXML = "issues.fxml";
    private UserRole role;
    @FXML private ProgressBar progressBar;
    @FXML private Button showOrdersButton;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        UserAPIHelper.get(UserAPIHelper.Operation.CHECK_LOGGED_USER_ROLE, BodyCodec.json(UserRole.class))
                .onSuccess(res -> {
                    this.role = res.body();
                    switch (this.role) {
                        case COURIER:
                            break;
                        case MAINTAINER:
                            Platform.runLater(() -> this.showOrdersButton.setVisible(false));
                            break;
                        default:
                    }
                });
    }

    @FXML
    private void logout() {
        UserAPIHelper.postJson(UserAPIHelper.Operation.LOG_OUT).onSuccess(res -> Platform.runLater(() -> {
            CastHelper.safeCast(this.progressBar.getScene().getWindow(), Stage.class).ifPresent(Stage::close);
            this.show(LOGIN_FXML, "Login", true, LOGIN_MIN_WIDTH, LOGIN_MIN_HEIGHT);
        }));
    }

    @FXML
    private void showOrders() {
        this.progressBar.setVisible(true);
        this.showOrdersButton.setDisable(true);
        this.progressBar.setVisible(false);
        this.showOrdersButton.setDisable(false);
        Platform.runLater(() -> this.show(ORDERS_FXML, "Orders", false, ORDERS_MIN_WIDTH,
                ORDERS_MIN_HEIGHT));
    }

    @FXML
    private void showReports() {
        if (this.role == UserRole.COURIER)
            this.show(NEGLIGENCE_DATA_FXML, "Reports", false, NEGLIGENCE_DATA_MIN_WIDTH,
                    NEGLIGENCE_DATA_MIN_HEIGHT);
        else if (this.role == UserRole.MAINTAINER)
            this.show(NEGLIGENCE_FXML, "Reports", false, NEGLIGENCE_MIN_WIDTH,
                    NEGLIGENCE_MIN_HEIGHT);
    }

    @FXML
    private void showIssues() {
        this.show(ISSUES_FXML, "Issues", false, ISSUES_MIN_WIDTH, ISSUES_MIN_HEIGHT);
    }

    private void show(final String fxml, final String title, final boolean closeable, final double minWidth,
                      final double minHeight) {
        final URL fileUrl = UserController.class.getResource(fxml);
        final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
        final Modality modality = closeable ? Modality.NONE : Modality.WINDOW_MODAL;
        FXHelper.initializeWindow(modality, title, fxmlLoader, minWidth, minHeight).ifPresent(Stage::show);
    }
}
