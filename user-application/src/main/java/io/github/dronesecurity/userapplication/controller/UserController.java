/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.auth.entities.Role;
import io.github.dronesecurity.userapplication.shipping.courier.CourierShippingService;
import io.github.dronesecurity.userapplication.utilities.CastHelper;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import io.github.dronesecurity.userapplication.utilities.UserHelper;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller dedicated to manage both {@link io.github.dronesecurity.userapplication.auth.entities.Courier} and
 * {@link io.github.dronesecurity.userapplication.auth.entities.Maintainer} actions.
 */
public class UserController implements Initializable {

    private static final String LOGIN_FXML = "login.fxml";
    private static final String ORDERS_FXML = "orders.fxml";
    private static final String NEGLIGENCE_FXML = "negligence.fxml";
    private static final String NEGLIGENCE_DATA_FXML = "negligenceData.fxml";
    private static final String ISSUES_FXML = "issues.fxml";
    private final Role role;
    @FXML private ProgressBar progressBar;
    @FXML private Button showOrdersButton;

    /**
     * Build the controller.
     */
    public UserController() {
        this.role = UserHelper.logged().getRole();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        switch (this.role) {
            case COURIER:
                break;
            case MAINTAINER:
                this.showOrdersButton.setVisible(false);
                break;
            default:
        }
    }

    @FXML
    private void logout() {
        UserHelper.logout();
        CastHelper.safeCast(this.progressBar.getScene().getWindow(), Stage.class).ifPresent(Stage::close);
        this.show(LOGIN_FXML, "Login", true);
    }

    @FXML
    private void showOrders() {
        this.progressBar.setVisible(true);
        this.showOrdersButton.setDisable(true);
        VertxHelper.VERTX.deployVerticle(CourierShippingService.class.getName()).onComplete(res -> {
            this.progressBar.setVisible(false);
            this.showOrdersButton.setDisable(false);
            if (res.succeeded())
                Platform.runLater(() -> this.show(ORDERS_FXML, "Orders", false));
            else
                LoggerFactory.getLogger(getClass()).error("Error creating the service:",
                        res.cause());
        });
    }

    @FXML
    private void showReports() {
        if (this.role == Role.COURIER)
            this.show(NEGLIGENCE_DATA_FXML, "Reports", false);
        else if (this.role == Role.MAINTAINER)
            this.show(NEGLIGENCE_FXML, "Reports", false);
    }

    @FXML
    private void showIssues() {
        this.show(ISSUES_FXML, "Issues", false);
    }

    private void show(final String fxml, final String title, final boolean closeable) {
        final URL fileUrl = UserController.class.getResource(fxml);
        final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
        final Modality modality = closeable ? Modality.NONE : Modality.WINDOW_MODAL;
        FXHelper.initializeWindow(modality, title, fxmlLoader).ifPresent(Stage::show);
    }
}
