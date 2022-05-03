package it.unibo.dronesecurity.userapplication.controller;

import it.unibo.dronesecurity.lib.AlertUtils;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.auth.entities.Role;
import it.unibo.dronesecurity.userapplication.auth.entities.User;
import it.unibo.dronesecurity.userapplication.auth.repo.AuthenticationRepository;
import it.unibo.dronesecurity.userapplication.shipping.courier.CourierShippingService;
import it.unibo.dronesecurity.userapplication.utilities.ClientHelper;
import it.unibo.dronesecurity.userapplication.utilities.FXHelper;
import it.unibo.dronesecurity.userapplication.utilities.UserHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller dedicated to {@link User} authentication.
 */
public final class AuthenticationController implements Initializable {

    private static final String COURIER_FXML = "orders.fxml";
    private static final String MAINTAINER_FXML = "maintainer.fxml";
    private static final String UNEXPECTED_VALUE = "Unexpected value: ";
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private Glyph showPasswordGlyph;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Button loginButton;
    @FXML private ProgressBar progressBar;
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
        this.progressBar.setVisible(true);
        this.loginButton.setDisable(true);
        final User user = this.userFromFields();
        AuthenticationRepository.getInstance().authenticate(user)
                .onSuccess(isLogged -> {
                    if (Boolean.TRUE.equals(isLogged)) {
                        UserHelper.setLoggedUser(user); // TODO retrieve all information from db(?)
                        switch (user.getRole()) {
                            case COURIER:
                                new CourierShippingService().startListening().onComplete(res -> {
                                    if (res.succeeded())
                                        this.showNextWindow(user.getRole());
                                    else
                                        LoggerFactory.getLogger(getClass()).error("Error creating the service:",
                                                res.cause());
                                    this.activeLogin();
                                });
                                break;
                            case MAINTAINER:
                                this.showNextWindow(user.getRole());
                                this.activeLogin();
                                break;
                            default:
                                throw new IllegalStateException(UNEXPECTED_VALUE + user.getRole());
                        }
                    } else {
                        AlertUtils.showErrorAlert("Username and/or passwords and/or role are wrong!");
                        this.activeLogin();
                    }
                });
    }

    @FXML
    private void keyPressed(final @NotNull KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && !this.loginButton.isDisabled())
            this.login();
    }

    @Contract(" -> new")
    private @NotNull User userFromFields() {
        final String username = this.usernameField.getText();
        final String password = this.passwordField.getText();
        switch (this.roleComboBox.getValue()) {
            case COURIER:
                return Courier.forAuthentication(username, password);
            case MAINTAINER:
                return Maintainer.complete(username, password);
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + this.roleComboBox.getValue());
        }
    }

    private void showNextWindow(final @NotNull Role role) {
        Platform.runLater(() -> {
            final String fxml;
            final String title;
            switch (role) {
                case COURIER:
                    fxml = COURIER_FXML;
                    title = "Orders";
                    break;
                case MAINTAINER:
                    fxml = MAINTAINER_FXML;
                    title = "Maintainer";
                    break;
                default:
                    throw new IllegalStateException(UNEXPECTED_VALUE + role);
            }
            ((Stage) this.loginButton.getScene().getWindow()).close();
            final URL fileUrl = getClass().getResource(fxml);
            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
            final Optional<Stage> optionalStage = FXHelper.createWindow(Modality.NONE, title, fxmlLoader);
            optionalStage.ifPresent(stage -> {
                stage.setOnCloseRequest(event -> {
                    ClientHelper.WEB_CLIENT.close();
                    Connection.getInstance().closeConnection();
                    Platform.exit();
                    System.exit(0);
                });
                stage.show();
            });
        });
    }

    private void activeLogin() {
        this.progressBar.setVisible(false);
        this.loginButton.setDisable(false);
    }
}
