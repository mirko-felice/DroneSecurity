package it.unibo.dronesecurity.userapplication.controller;

import it.unibo.dronesecurity.lib.AlertUtils;
import it.unibo.dronesecurity.userapplication.auth.entities.Role;
import it.unibo.dronesecurity.userapplication.auth.entities.BaseUser;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.auth.repo.AuthenticationRepository;
import it.unibo.dronesecurity.userapplication.utilities.ClientHelper;
import it.unibo.dronesecurity.userapplication.utilities.UserHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller dedicated to {@link BaseUser} authentication.
 */
public final class AuthenticationController implements Initializable {

    private static final String COURIER_FXML = "orders.fxml";
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private Glyph showPasswordGlyph;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Button loginButton;
    private boolean isPasswordShown;

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
                    if (Boolean.TRUE.equals(isLogged))
                        try {
                            UserHelper.set(user);
                            ((Stage) this.loginButton.getScene().getWindow()).close();
                            final URL fileUrl = getClass().getResource(COURIER_FXML);
                            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
                            fxmlLoader.setController(new OrdersController());
                            final Scene scene = new Scene(fxmlLoader.load());
                            final Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.setTitle("Orders");
                            stage.setOnCloseRequest(event -> {
                                ClientHelper.WEB_CLIENT.close();
                                Platform.exit();
                                System.exit(0);
                            });
                            stage.show();
                        } catch (IOException e) {
                            LoggerFactory.getLogger(getClass()).error("Error creating the new window:", e);
                        }
                    else
                        AlertUtils.showErrorAlert("Username and/or passwords are wrong!");
                }));
    }
}
