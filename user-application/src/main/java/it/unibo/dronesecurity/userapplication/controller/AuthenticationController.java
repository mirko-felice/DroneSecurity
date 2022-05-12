package it.unibo.dronesecurity.userapplication.controller;

import it.unibo.dronesecurity.lib.AlertUtils;
import it.unibo.dronesecurity.userapplication.auth.entities.NotLoggedUser;
import it.unibo.dronesecurity.userapplication.auth.entities.NotLoggedUserImpl;
import it.unibo.dronesecurity.userapplication.auth.entities.Role;
import it.unibo.dronesecurity.userapplication.auth.entities.User;
import it.unibo.dronesecurity.userapplication.auth.repo.AuthenticationRepository;
import it.unibo.dronesecurity.userapplication.shipping.courier.CourierShippingService;
import it.unibo.dronesecurity.userapplication.utilities.FXHelper;
import it.unibo.dronesecurity.userapplication.utilities.UserHelper;
import it.unibo.dronesecurity.userapplication.utilities.VertxHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Controller dedicated to {@link User} authentication.
 */
public final class AuthenticationController {

    private static final String COURIER_FXML = "orders.fxml";
    private static final String NEGLIGENCE_FXML = "negligence.fxml";
    private static final String UNEXPECTED_VALUE = "Unexpected value: ";
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private Glyph showPasswordGlyph;
    @FXML private Button loginButton;
    @FXML private ProgressBar progressBar;
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
        this.progressBar.setVisible(true);
        this.loginButton.setDisable(true);
        final NotLoggedUser notLoggedUser = this.userFromFields();
        AuthenticationRepository.getInstance().authenticate(notLoggedUser)
                .onSuccess(loggedUser -> {
                    UserHelper.setLoggedUser(loggedUser);
                    this.showNextWindow(loggedUser.getRole());
                })
                .onFailure(e -> {
                    AlertUtils.showErrorAlert("Username and/or passwords and/or role are wrong!");
                    this.activeLogin();
                });
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

    private void showNextWindow(final @NotNull Role role) {
        switch (role) {
            case COURIER:
                VertxHelper.VERTX.deployVerticle(CourierShippingService.class.getName()).onComplete(res -> {
                    if (res.succeeded())
                        this.show(COURIER_FXML, "Orders");
                    else
                        LoggerFactory.getLogger(getClass()).error("Error creating the service:",
                                res.cause());
                    this.activeLogin();
                });
                break;
            case MAINTAINER:
                this.show(NEGLIGENCE_FXML, "Maintainer");
                this.activeLogin();
                break;
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + role);
        }
    }

    private void show(final String fxml, final String title) {
        Platform.runLater(() -> {
            ((Stage) this.loginButton.getScene().getWindow()).close();
            final URL fileUrl = getClass().getResource(fxml);
            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
            FXHelper.initializeWindow(Modality.NONE, title, fxmlLoader).ifPresent(Stage::show);
        });
    }

    private void activeLogin() {
        this.progressBar.setVisible(false);
        this.loginButton.setDisable(false);
    }
}
