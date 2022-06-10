/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.auth.AuthenticationService;
import io.github.dronesecurity.userapplication.auth.entities.NotLoggedUser;
import io.github.dronesecurity.userapplication.auth.entities.NotLoggedUserImpl;
import io.github.dronesecurity.userapplication.auth.entities.User;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import io.github.dronesecurity.userapplication.utilities.UserHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * Controller dedicated to {@link User} authentication.
 */
public final class AuthenticationController {

    private static final String USER_FXML = "user.fxml";
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private Glyph showPasswordGlyph;
    @FXML private Button loginButton;
    private boolean isPasswordShown;

    @FXML
    private void showPassword() {
        this.isPasswordShown = !this.isPasswordShown;
        this.visiblePasswordField.setVisible(this.isPasswordShown);
        this.passwordField.setVisible(!this.isPasswordShown);
        this.showPasswordGlyph.setIcon(this.isPasswordShown ? FontAwesome.Glyph.EYE : FontAwesome.Glyph.EYE_SLASH);
        if (this.isPasswordShown)
            this.visiblePasswordField.setText(this.passwordField.getText());
        else
            this.passwordField.setText(this.visiblePasswordField.getText());
    }

    @FXML
    private void login() {
        final NotLoggedUser notLoggedUser = this.userFromFields();
        AuthenticationService.getInstance().authenticate(notLoggedUser)
                .onSuccess(loggedUser -> {
                    UserHelper.loggedIn(loggedUser);
                    Platform.runLater(() -> {
                        ((Stage) this.loginButton.getScene().getWindow()).close();
                        final URL fileUrl = getClass().getResource(USER_FXML);
                        final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
                        FXHelper.initializeWindow(Modality.NONE, "User Menu", fxmlLoader).ifPresent(Stage::show);
                    });
                })
                .onFailure(e ->
                        DialogUtils.showErrorDialog("Username and/or passwords and/or role are wrong!"));
    }

    @FXML
    private void keyPressed(final @NotNull KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && !this.loginButton.isDisabled())
            this.login();
    }

    @Contract(" -> new")
    private @NotNull NotLoggedUser userFromFields() {
        final String username = this.usernameField.getText();
        final String password = this.passwordField.isVisible()
                ? this.passwordField.getText()
                : this.visiblePasswordField.getText();
        return new NotLoggedUserImpl(username, password);
    }

}
