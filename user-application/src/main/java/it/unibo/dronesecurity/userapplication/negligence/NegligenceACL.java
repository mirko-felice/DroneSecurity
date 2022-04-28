package it.unibo.dronesecurity.userapplication.negligence;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.repo.AuthenticationRepository;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.CountDownLatch;

/**
 * Anti-Corruption Layer dedicated to help the Negligence Reporting Context to transform data received by the Drone
 * Context.
 */
public final class NegligenceACL {

    private static final CountDownLatch LATCH = new CountDownLatch(1);

    private NegligenceACL() { }

    /**
     * Retrieve the {@link Courier} starting from his username.
     * @param courier courier's username
     * @return the courier
     */
    @Contract("_ -> new")
    public static Courier retrieveCourier(final String courier) throws InterruptedException {
        final Future<Courier> future = AuthenticationRepository.getInstance().retrieveCourierFromUsername(courier)
                .onSuccess(unused -> LATCH.countDown());
        LATCH.await();
        return future.result();
    }
}
