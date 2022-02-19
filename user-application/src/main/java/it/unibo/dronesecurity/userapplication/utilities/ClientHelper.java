package it.unibo.dronesecurity.userapplication.utilities;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

/**
 * Helper class containing {@link WebClient} singleton.
 */
public final class ClientHelper {

    /**
     * {@link WebClient} used to interact with services.
     */
    public static final WebClient WEB_CLIENT = WebClient.create(Vertx.vertx());

    private ClientHelper() { }
}
