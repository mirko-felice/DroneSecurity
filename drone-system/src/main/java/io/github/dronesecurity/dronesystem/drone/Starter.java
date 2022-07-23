/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import io.github.dronesecurity.dronesystem.drone.application.drone.DroneRemoteCommands;
import io.github.dronesecurity.dronesystem.drone.presentation.PropertiesGenerator;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.PropertiesConstants;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Application that activates the Drone application.
 */
public final class Starter {

    private static final Scanner SCANNER = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final PrintWriter WRITER = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

    private Starter() { }

    /**
     * Main method for drone activation.
     *
     * @param args the args that are passed to the application
     */
    public static void main(final String[] args) {
        final File propertiesFile = new File(PropertiesConstants.PROPERTIES_FILENAME);

        if (Arrays.asList(args).contains("generate-properties")) {
            if (!propertiesFile.exists() || wantToResetProperties())
                new PropertiesGenerator().generateProperties();
        } else
        if (!propertiesFile.exists())
            new PropertiesGenerator().generateProperties();

        connectAndStart();
    }

    private static void connectAndStart() {
        Connection.getInstance().connect();
        SCANNER.close();
        new DroneRemoteCommands().waitForActivation();
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
}
