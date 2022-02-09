package it.unibo.dronesecurity.lib;

/**
 * Property keys needed to create the correct {@link Connection}.
 */
public final class PropertiesConstants {

    /**
     * Name of the file containing the properties.
     */
    public static final String PROPERTIES_FILENAME = "project.properties";

    /**
     * Path of the folder containing all the certificates.
     */
    public static final String CERTS_FOLDER_PATH = "certsFolderPath";

    /**
     * Name of the file containing the certificate of the Drone.
     */
    public static final String CERTIFICATE_FILENAME = "certificateFile";

    /**
     * Name of the file containing the private key of the Drone.
     */
    public static final String PRIVATE_KEY_FILENAME = "privateKeyFile";

    /**
     * Name of the file containing the certificate of the root authority.
     */
    public static final String CERTIFICATE_AUTHORITY_FILENAME = "certificateAuthorityFile";

    /**
     * Endpoint representing the Drone itself.
     */
    public static final String ENDPOINT = "endpoint";

    /**
     * Client identifier.
     */
    public static final String CLIENT_ID = "clientID";

    private PropertiesConstants() { }
}
