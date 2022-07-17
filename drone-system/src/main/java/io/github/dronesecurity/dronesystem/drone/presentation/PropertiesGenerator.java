/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.presentation;

import io.github.dronesecurity.lib.PropertiesConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Scanner;

/**
 * Service that allows to set the properties for AWS connection.
 */
public class PropertiesGenerator {

    private static final Scanner SCANNER = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final PrintWriter WRITER = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

    /**
     * Follows the user in setting the properties to eventually make a correct AWS connection.
     */
    public void generateProperties() {
        WRITER.println("Properties creation...");
        WRITER.println("Enter Client identifier for this connection:");
        final String clientId = this.getStringParameter("Client Identifier must NOT be empty!");

        WRITER.println("Enter endpoint for the iot service:");
        final String endpoint = this.getStringParameter("Endpoint must NOT be empty!");

        WRITER.println("Enter certificates folder path:");
        String certFolderPath;
        do {
            certFolderPath = SCANNER.nextLine();

            if (!Files.isDirectory(Path.of(certFolderPath)))
                WRITER.println("Specified path does not denote a directory.");
        } while (!Files.isDirectory(Path.of(certFolderPath)));
        final File certFolder = new File(certFolderPath);

        WRITER.println("Enter drone certificate file name:");
        final File droneCert =
                this.getCertificateFilePath(certFolderPath, "Certificate File is NOT in the right folder!");

        WRITER.println("Enter private key file name:");
        final File privateKey =
                this.getCertificateFilePath(certFolderPath, "Private Key File is NOT in the right folder!");

        WRITER.println("Enter root certificate authority:");
        final File rootCA =
                this.getCertificateFilePath(certFolderPath, "Certificate Authority File is NOT in the right folder!");

        final Properties properties = new Properties();
        try (OutputStream outputStream = Files.newOutputStream(Path.of(PropertiesConstants.PROPERTIES_FILENAME))) {
            properties.setProperty(PropertiesConstants.CERTS_FOLDER_PATH, certFolder.getAbsolutePath() + SEP);
            properties.setProperty(PropertiesConstants.CERTIFICATE_FILENAME, droneCert.getName());
            properties.setProperty(PropertiesConstants.PRIVATE_KEY_FILENAME, privateKey.getName());
            properties.setProperty(PropertiesConstants.CERTIFICATE_AUTHORITY_FILENAME, rootCA.getName());
            properties.setProperty(PropertiesConstants.ENDPOINT, endpoint);
            properties.setProperty(PropertiesConstants.CLIENT_ID, clientId);
            properties.store(outputStream, "Connection settings");
        } catch (IOException e) {
            LoggerFactory.getLogger(PropertiesGenerator.class).error("Can NOT write to file .properties.", e);
        }
    }

    private String getStringParameter(final String errorMessage) {
        String parameter;
        do {
            parameter = SCANNER.nextLine();
            if (parameter == null || parameter.isEmpty())
                WRITER.println(errorMessage);
        } while (parameter == null || parameter.isEmpty());
        return parameter;
    }

    @Contract("_, _ -> new")
    private @NotNull File getCertificateFilePath(final String certFolderPath, final String errorMessage) {
        String certificateFileName;
        do {
            certificateFileName = SCANNER.nextLine();

            if (!Files.exists(Path.of(certFolderPath + SEP + certificateFileName))
                    || this.isNotCertFile(certificateFileName))
                WRITER.println(errorMessage);
        } while (!Files.exists(Path.of(certFolderPath + SEP + certificateFileName))
                || this.isNotCertFile(certificateFileName));
        return new File(certFolderPath + SEP + certificateFileName);
    }

    private boolean isNotCertFile(final @NotNull String fileToCheck) {
        return !fileToCheck.endsWith(".pem")
                && !fileToCheck.endsWith(".crt")
                && !fileToCheck.endsWith(".key");
    }
}
