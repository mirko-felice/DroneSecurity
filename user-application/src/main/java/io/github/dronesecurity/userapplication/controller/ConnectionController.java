/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.PropertiesConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.crt.CrtRuntimeException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Controller dedicated to set the {@link Connection} settings.
 */
public final class ConnectionController implements Initializable {

    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("*.pem", "*.crt", "*.key");
    @FXML private Label certsFolderLabel;
    @FXML private Label certificateFileLabel;
    @FXML private Label privateKeyFileLabel;
    @FXML private Label certificateAuthorityFileLabel;
    @FXML private Button saveSettingsButton;
    @FXML private TextField endpointTextField;
    @FXML private TextField clientIdTextField;
    @FXML private ProgressBar progressBar;
    private final Properties properties = new Properties();
    private DirectoryChooser directoryChooser;
    private FileChooser fileChooser;
    private File certsFolder;
    private File certificateFile;
    private File privateKeyFile;
    private File certificateAuthorityFile;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.directoryChooser = new DirectoryChooser();
        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Certificate file", ALLOWED_EXTENSIONS));
        this.endpointTextField.setText("");
        if (new File(PropertiesConstants.PROPERTIES_FILENAME).exists())
            try (InputStream inputStream = Files.newInputStream(Path.of(PropertiesConstants.PROPERTIES_FILENAME))) {
                this.properties.load(inputStream);

                final String certsFolderPath = this.properties.getProperty(PropertiesConstants.CERTS_FOLDER_PATH) + SEP;
                final String certificateFileName =
                        this.properties.getProperty(PropertiesConstants.CERTIFICATE_FILENAME);
                final String privateKeyFileName = this.properties.getProperty(PropertiesConstants.PRIVATE_KEY_FILENAME);
                final String certificateAuthorityFileName =
                        this.properties.getProperty(PropertiesConstants.CERTIFICATE_AUTHORITY_FILENAME);

                this.certsFolder = new File(certsFolderPath);
                this.certificateFile = new File(certsFolderPath, certificateFileName);
                this.privateKeyFile = new File(certsFolderPath, privateKeyFileName);
                this.certificateAuthorityFile = new File(certsFolderPath, certificateAuthorityFileName);

                this.certsFolderLabel.setText(certsFolderPath.replace("\\\\", "\\"));
                this.certificateFileLabel.setText(certificateFileName);
                this.privateKeyFileLabel.setText(privateKeyFileName);
                this.certificateAuthorityFileLabel.setText(certificateAuthorityFileName);
                this.endpointTextField.setText(this.properties.getProperty(PropertiesConstants.ENDPOINT));
                this.clientIdTextField.setText(this.properties.getProperty(PropertiesConstants.CLIENT_ID));
            } catch (IOException e) {
                LoggerFactory.getLogger(getClass()).error("Can NOT read file .properties.", e);
            }
    }

    @FXML
    private void chooseCertsFolder() {
        final File newCertsFolder = this.directoryChooser.showDialog(this.saveSettingsButton.getScene().getWindow());
        if (newCertsFolder != null)
            this.certsFolder = newCertsFolder;
        if (this.certsFolder != null)
            this.certsFolderLabel.setText(this.certsFolder.getPath());
    }

    @FXML
    private void chooseCertificateFile() {
        final File newCertificateFile = this.fileChooser.showOpenDialog(this.saveSettingsButton.getScene().getWindow());
        if (newCertificateFile != null)
            this.certificateFile = newCertificateFile;
        if (this.certificateFile != null)
            this.certificateFileLabel.setText(this.certificateFile.getName());
    }

    @FXML
    private void choosePrivateKeyFile() {
        final File newPrivateKeyFile = this.fileChooser.showOpenDialog(this.saveSettingsButton.getScene().getWindow());
        if (newPrivateKeyFile != null)
            this.privateKeyFile = newPrivateKeyFile;
        if (this.privateKeyFile != null)
            this.privateKeyFileLabel.setText(this.privateKeyFile.getName());
    }

    @FXML
    private void chooseCertificateAuthorityFile() {
        final File newCertificateAuthorityFile =
                this.fileChooser.showOpenDialog(this.saveSettingsButton.getScene().getWindow());
        if (newCertificateAuthorityFile != null)
            this.certificateAuthorityFile = newCertificateAuthorityFile;
        if (this.certificateAuthorityFile != null)
            this.certificateAuthorityFileLabel.setText(this.certificateAuthorityFile.getName());
    }

    @FXML
    private void saveSettings() {
        try {
            if (this.arePropertiesValid()) {
                this.save();
                this.progressBar.setVisible(true);
                Connection.getInstance().connect().whenComplete((result, t) -> Platform.runLater(() -> {
                    if (t == null && !result)
                        ((Stage) this.saveSettingsButton.getScene().getWindow()).close();
                    else if (t != null)
                        DialogUtils.showErrorDialog("Wrong endpoint!");
                    this.progressBar.setVisible(false);
                }));
            } else {
                DialogUtils.showWarningDialog("Fill connection settings!");
            }
        } catch (CrtRuntimeException e) {
            DialogUtils.showErrorDialog("One or more certificates are not valid!");
        } catch (IllegalArgumentException e) {
            DialogUtils.showErrorDialog(e.getMessage());
        }
    }

    private boolean arePropertiesValid() {
        final boolean isClientIDValid = this.clientIdTextField.getText() != null
                && !this.clientIdTextField.getText().isEmpty();
        if (!isClientIDValid)
            DialogUtils.showErrorDialog("Client Identifier must NOT be empty!");
        if (this.areFieldsFilled())
            return false;
        final String certsFolderPlusSep = this.certsFolder.getPath() + SEP;
        final boolean isCertificateFileInRightFolder = this.certificateFile.getPath()
                .equals(certsFolderPlusSep + this.certificateFile.getName());
        final boolean isPrivateKeyFileInRightFolder = this.privateKeyFile.getPath()
                .equals(certsFolderPlusSep + this.privateKeyFile.getName());
        final boolean isCertificateAuthorityFileInRightFolder = this.certificateAuthorityFile.getPath()
                .equals(certsFolderPlusSep + this.certificateAuthorityFile.getName());
        if (!isCertificateFileInRightFolder)
            DialogUtils.showErrorDialog("Certificate File is NOT in the right folder!");
        if (!isPrivateKeyFileInRightFolder)
            DialogUtils.showErrorDialog("Private Key File is NOT in the right folder!");
        if (!isCertificateAuthorityFileInRightFolder)
            DialogUtils.showErrorDialog("Certificate Authority File is NOT in the right folder!");
        return isCertificateFileInRightFolder
                && isPrivateKeyFileInRightFolder
                && isCertificateAuthorityFileInRightFolder
                && isClientIDValid;
    }

    private boolean areFieldsFilled() {
        return this.certsFolder == null
                || this.certificateFile == null
                || this.privateKeyFile == null
                || this.certificateAuthorityFile == null
                || this.endpointTextField.getText().isEmpty();
    }

    private void save() {
        try (OutputStream outputStream = Files.newOutputStream(Path.of(PropertiesConstants.PROPERTIES_FILENAME))) {
            this.properties.setProperty(PropertiesConstants.CERTS_FOLDER_PATH, this.certsFolder.getPath() + SEP);
            this.properties.setProperty(PropertiesConstants.CERTIFICATE_FILENAME, this.certificateFile.getName());
            this.properties.setProperty(PropertiesConstants.PRIVATE_KEY_FILENAME, this.privateKeyFile.getName());
            this.properties.setProperty(PropertiesConstants.CERTIFICATE_AUTHORITY_FILENAME,
                    this.certificateAuthorityFile.getName());
            this.properties.setProperty(PropertiesConstants.ENDPOINT, this.endpointTextField.getText());
            this.properties.setProperty(PropertiesConstants.CLIENT_ID, this.clientIdTextField.getText());
            this.properties.store(outputStream, "Connection settings");
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Can NOT write to file .properties.", e);
        }
    }
}
