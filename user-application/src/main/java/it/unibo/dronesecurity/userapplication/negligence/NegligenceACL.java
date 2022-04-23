package it.unibo.dronesecurity.userapplication.negligence;

import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Anti-Corruption Layer dedicated to help the Negligence Reporting Context to transform data received by the Drone
 * Context.
 */
public final class NegligenceACL {

    private NegligenceACL() { }

    /**
     * Retrieve the {@link Courier} starting from his username.
     * @param courier courier's username
     * @return the courier
     */
    @Contract("_ -> new")
    public static @NotNull Courier retrieveCourier(final String courier) {
        return new Courier(courier, null); // TODO maybe get info from db
    }
}
