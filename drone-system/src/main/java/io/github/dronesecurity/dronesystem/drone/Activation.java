/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import io.github.dronesecurity.lib.Connection;
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
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

/**
 * Application that activates Drone Service.
 */
public final class Activation {

    private static final Scanner SCANNER = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final String SEP = FileSystems.getDefault().getSeparator();
    private static final PrintWriter WRITER = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

    private Activation() { }

    /**
     * Main method for drone activation.
     *
     * @param args the args that are passed to the application
     */
    public static void main(final String[] args) {
        final File propertiesFile = new File(PropertiesConstants.PROPERTIES_FILENAME);

        if (Arrays.asList(args).contains("generate-properties")) {
            if (!propertiesFile.exists() || wantToResetProperties())
                generateProperties();
        } else
            if (!propertiesFile.exists())
                generateProperties();

        connectAndStart();
    }

    private static void connectAndStart() {
        Connection.getInstance().connect();
        SCANNER.close();
        new DroneService().waitForDeliveryAssignment();
    }

    private static void generateProperties() {
        WRITER.println("Properties creation...");
        WRITER.println("Enter Client identifier for this connection:");
        final String clientId = getStringParameter("Client Identifier must NOT be empty!");

        WRITER.println("Enter endpoint for the iot service:");
        final String endpoint = getStringParameter("Endpoint must NOT be empty!");

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
                getCertificateFilePath(certFolderPath, "Certificate File is NOT in the right folder!");

        WRITER.println("Enter private key file name:");
        final File privateKey =
                getCertificateFilePath(certFolderPath, "Private Key File is NOT in the right folder!");

        WRITER.println("Enter root certificate authority:");
        final File rootCA =
                getCertificateFilePath(certFolderPath, "Certificate Authority File is NOT in the right folder!");

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
            LoggerFactory.getLogger(Activation.class).error("Can NOT write to file .properties.", e);
        }
    }

    private static boolean wantToResetProperties() {
        WRITER.println("File properties already found!");
        String res;
        do {
            WRITER.println("Would you like to reset values? (y/n)");
            res = SCANNER.nextLine();
        } while (!"y".equals(res) && !"n".equals(res));
        return "y".equals(res);
    }

    private static String getStringParameter(final String errorMessage) {
        String parameter;
        do {
            parameter = SCANNER.nextLine();
            if (parameter == null || parameter.isEmpty())
                WRITER.println(errorMessage);
        } while (parameter == null || parameter.isEmpty());
        return parameter;
    }

    @Contract("_, _ -> new")
    private static @NotNull File getCertificateFilePath(final String certFolderPath, final String errorMessage) {
        String certificateFileName;
        do {
            certificateFileName = SCANNER.nextLine();

            if (!Files.exists(Path.of(certFolderPath + SEP + certificateFileName))
                    || isNotCertFile(certificateFileName))
                WRITER.println(errorMessage);
        } while (!Files.exists(Path.of(certFolderPath + SEP + certificateFileName))
                || isNotCertFile(certificateFileName));
        return new File(certFolderPath + SEP + certificateFileName);
    }

    private static boolean isNotCertFile(final @NotNull String fileToCheck) {
        return !fileToCheck.endsWith(".pem")
                && !fileToCheck.endsWith(".crt")
                && !fileToCheck.endsWith(".key");
    }
}
