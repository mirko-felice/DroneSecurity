package it.unibo.dronesecurity.userapplication.controller;

import io.vertx.ext.web.client.WebClient;
import it.unibo.dronesecurity.userapplication.controller.auth.Role;
import it.unibo.dronesecurity.userapplication.controller.auth.entities.BaseUser;
import it.unibo.dronesecurity.userapplication.controller.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.controller.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.controller.auth.repo.AuthenticationRepository;
import it.unibo.dronesecurity.userapplication.utilities.AlertUtils;
import it.unibo.dronesecurity.userapplication.utilities.CustomLogger;
import it.unibo.dronesecurity.userapplication.utilities.LoggedUser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller dedicated to {@link BaseUser} authentication.
 */
public final class AuthenticationController implements Initializable {

    private static final String COURIER_FXML = "orders.fxml";
    private final transient WebClient client;
    @FXML private transient TextField usernameField;
    @FXML private transient PasswordField passwordField;
    @FXML private transient TextField visiblePasswordField;
    @FXML private transient Glyph showPasswordGlyph;
    @FXML private transient ComboBox<Role> roleComboBox;
    @FXML private transient Button loginButton;
    @FXML private transient Label validationLabel;
    private transient boolean isPasswordShown;

    /**
     * Build the controller.
     * @param client the client to interact with services
     */
    public AuthenticationController(final WebClient client) {
        this.client = client;
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.roleComboBox.getItems().addAll(Role.COURIER, Role.MAINTAINER);
        this.roleComboBox.getSelectionModel().selectFirst();
    }

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
        final String username = this.usernameField.getText();
        final String password = this.passwordField.getText();
        final BaseUser user;
        switch (this.roleComboBox.getValue()) {
            case COURIER:
                user = new Courier(username, password);
                break;
            case MAINTAINER:
                user = new Maintainer(username, password);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.roleComboBox.getValue());
        }
        AuthenticationRepository.getInstance().authenticate(user)
                .onSuccess(isLogged -> Platform.runLater(() -> {
                    if (isLogged)
                        try {
                            LoggedUser.set(user);
                            ((Stage) this.loginButton.getScene().getWindow()).close();
                            final URL fileUrl = getClass().getResource(COURIER_FXML);
                            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
                            fxmlLoader.setController(new StartController(this.client));
                            final Scene scene = new Scene(fxmlLoader.load());
                            final Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.setTitle("Orders");
                            stage.setOnCloseRequest(event -> {
                                this.client.close();
                                Platform.exit();
                                System.exit(0);
                            });
                            stage.show();
                        } catch (IOException e) {
                            CustomLogger.getLogger(getClass().getName())
                                    .severe("Error creating the new window:", e);
                        }
                    else
                        AlertUtils.showErrorAlert("Username and/or passwords are wrong!");
                }));
    }
}
