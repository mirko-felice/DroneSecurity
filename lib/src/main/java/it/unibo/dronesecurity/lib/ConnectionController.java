package it.unibo.dronesecurity.lib;

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
import software.amazon.awssdk.crt.CrtRuntimeException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Controller dedicated to set the {@link Connection} settings.
 */
public final class ConnectionController implements Initializable {

    private static final String SEP = FileSystems.getDefault().getSeparator();
    @FXML private transient Label certsFolderLabel;
    @FXML private transient Label certificateFileLabel;
    @FXML private transient Label privateKeyFileLabel;
    @FXML private transient Label certificateAuthorityFileLabel;
    @FXML private transient Button saveSettingsButton;
    @FXML private transient TextField endpointTextField;
    @FXML private transient TextField clientIdTextField;
    @FXML private transient ProgressBar progressBar;
    private final transient Properties properties = new Properties();
    private transient DirectoryChooser directoryChooser;
    private transient FileChooser fileChooser;
    private transient File certsFolder;
    private transient File certificateFile;
    private transient File privateKeyFile;
    private transient File certificateAuthorityFile;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.directoryChooser = new DirectoryChooser();
        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Certificate file", "*.pem"));
        this.endpointTextField.setText("");
        try {
            if (new File(PropertiesConstants.PROPERTIES_FILENAME).exists()) {
                this.properties.load(Files.newInputStream(Path.of(PropertiesConstants.PROPERTIES_FILENAME)));
                final String certsFolderPath = this.properties.getProperty(PropertiesConstants.CERTS_FOLDER_PATH) + SEP;
                final String certificateFileName =
                        this.properties.getProperty(PropertiesConstants.CERTIFICATE_FILENAME);
                final String privateKeyFileName = this.properties.getProperty(PropertiesConstants.PRIVATE_KEY_FILENAME);
                final String certificateAuthorityFileName =
                        this.properties.getProperty(PropertiesConstants.CERTIFICATE_AUTHORITY_FILENAME);

                this.certsFolder = new File(certsFolderPath);
                this.certificateFile = new File(certsFolderPath + certificateFileName);
                this.privateKeyFile = new File(certsFolderPath + privateKeyFileName);
                this.certificateAuthorityFile = new File(certsFolderPath + certificateAuthorityFileName);

                this.certsFolderLabel.setText(certsFolderPath.replace("\\\\", "\\"));
                this.certificateFileLabel.setText(certificateFileName);
                this.privateKeyFileLabel.setText(privateKeyFileName);
                this.certificateAuthorityFileLabel.setText(certificateAuthorityFileName);
                this.endpointTextField.setText(this.properties.getProperty(PropertiesConstants.ENDPOINT));
                this.clientIdTextField.setText(this.properties.getProperty(PropertiesConstants.CLIENT_ID));
            }
        } catch (IOException e) {
            CustomLogger.getLogger(getClass().getName()).severe(e.getMessage(), e);
        }
    }

    @FXML
    private void chooseCertsFolder() {
        final File newCertsFolder = this.directoryChooser.showDialog(this.saveSettingsButton.getScene().getWindow());
        if (newCertsFolder != null)
            this.certsFolder = newCertsFolder;
        this.certsFolderLabel.setText(this.certsFolder.getPath());
    }

    @FXML
    private void chooseCertificateFile() {
        final File newCertificateFile = this.fileChooser.showOpenDialog(this.saveSettingsButton.getScene().getWindow());
        if (newCertificateFile != null)
            this.certificateFile = newCertificateFile;
        this.certificateFileLabel.setText(this.certificateFile.getName());
    }

    @FXML
    private void choosePrivateKeyFile() {
        final File newPrivateKeyFile = this.fileChooser.showOpenDialog(this.saveSettingsButton.getScene().getWindow());
        if (newPrivateKeyFile != null)
            this.privateKeyFile = newPrivateKeyFile;
        this.privateKeyFileLabel.setText(this.privateKeyFile.getName());
    }

    @FXML
    private void chooseCertificateAuthorityFile() {
        final File newCertificateAuthorityFile =
                this.fileChooser.showOpenDialog(this.saveSettingsButton.getScene().getWindow());
        if (newCertificateAuthorityFile != null)
            this.certificateAuthorityFile = newCertificateAuthorityFile;
        this.certificateAuthorityFileLabel.setText(this.certificateAuthorityFile.getName());
    }

    @FXML
    private void saveSettings() {
        try {
            if (this.arePropertiesValid()) {
                this.save();
                this.progressBar.setVisible(true);
                Connection.getInstance().connect().whenComplete((result, t) -> Platform.runLater(() -> {
                    if (t == null && result)
                        ((Stage) this.saveSettingsButton.getScene().getWindow()).close();
                    else if (t != null)
                        AlertUtils.showErrorAlert("Wrong endpoint!");
                    this.progressBar.setVisible(false);
                }));
            }
        } catch (CrtRuntimeException e) {
            AlertUtils.showErrorAlert("One or more certificates are not valid!");
        } catch (IllegalArgumentException e) {
            AlertUtils.showErrorAlert(e.getMessage());
        }
    }

    private boolean arePropertiesValid() {
        final boolean isClientIDValid = this.clientIdTextField.getText() != null
                && !this.clientIdTextField.getText().isEmpty();
        if (!isClientIDValid)
            AlertUtils.showErrorAlert("Client Identifier must NOT be empty!");

        final String certsFolderPlusSep = this.certsFolder.getPath() + SEP;
        final boolean isCertificateFileInRightFolder = this.certificateFile.getPath()
                .equals(certsFolderPlusSep + this.certificateFile.getName());
        final boolean isPrivateKeyFileInRightFolder = this.privateKeyFile.getPath()
                .equals(certsFolderPlusSep + this.privateKeyFile.getName());
        final boolean isCertificateAuthorityFileInRightFolder = this.certificateAuthorityFile.getPath()
                .equals(certsFolderPlusSep + this.certificateAuthorityFile.getName());
        if (!isCertificateFileInRightFolder)
            AlertUtils.showErrorAlert("Certificate File is NOT in the right folder!");
        if (!isPrivateKeyFileInRightFolder)
            AlertUtils.showErrorAlert("Private Key File is NOT in the right folder!");
        if (!isCertificateAuthorityFileInRightFolder)
            AlertUtils.showErrorAlert("Certificate Authority File is NOT in the right folder!");
        return isCertificateFileInRightFolder
                && isPrivateKeyFileInRightFolder
                && isCertificateAuthorityFileInRightFolder
                && isClientIDValid;
    }

    private void save() {
        try {
            this.properties.setProperty(PropertiesConstants.CERTS_FOLDER_PATH, this.certsFolder.getPath() + SEP);
            this.properties.setProperty(PropertiesConstants.CERTIFICATE_FILENAME, this.certificateFile.getName());
            this.properties.setProperty(PropertiesConstants.PRIVATE_KEY_FILENAME, this.privateKeyFile.getName());
            this.properties.setProperty(PropertiesConstants.CERTIFICATE_AUTHORITY_FILENAME,
                    this.certificateAuthorityFile.getName());
            this.properties.setProperty(PropertiesConstants.ENDPOINT, this.endpointTextField.getText());
            this.properties.setProperty(PropertiesConstants.CLIENT_ID, this.clientIdTextField.getText());
            this.properties.store(Files.newOutputStream(Path.of(PropertiesConstants.PROPERTIES_FILENAME)),
                    "Connection settings");
        } catch (IOException e) {
            CustomLogger.getLogger(getClass().getName()).severe(e.getMessage(), e);
        }
    }
}
