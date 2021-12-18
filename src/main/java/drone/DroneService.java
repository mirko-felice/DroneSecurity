package drone;


import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service providing data about drone status and its sensors
 */
public class DroneService {

    private final static int PORT = 10001;

    //Drone
    private final Drone drone;
    private Thread dataAnalyzer;
    private boolean active;

    // Connection
    private final Vertx vertx = Vertx.vertx();
    private final List<ServerWebSocket> sockets;
    private final HttpServer server;
    private final Map<String, Double> sensorData;

    public DroneService() {
        this.drone = new Drone();
        this.sensorData = new HashMap<>();
        this.sockets = new ArrayList<>();
        this.initAnalyzer();
        this.active = false;

        //Init server
        this.server = vertx.createHttpServer();
        this.server.webSocketHandler(socket -> {
            socket.accept();

            System.out.println("New connection established with " + socket.host());

            sockets.add(socket);
        });
        this.server.listen(PORT);
    }

    /**
     * Activates the drone
     */
    public void startDrone() {
        this.active = true;
        this.dataAnalyzer.start();
    }

    /**
     * Provides data useful to connect to the drone
     */
    public int getConnectionData() {
        return PORT;
    }

    private void initAnalyzer() {
        this.dataAnalyzer = new Thread(() -> {
            List<ServerWebSocket> closedSockets = new ArrayList<>();
            try {
                while (this.active) {
                    drone.analyzeData();
                    sensorData.put("accelerometer", drone.getAccelerometerSensor().getReadableValue());
                    sensorData.put("proximity", drone.getProximitySensor().getReadableValue());
                    sensorData.put("camera", drone.getCameraSensor().getReadableValue());
                    System.out.println("Data obtained");
                    for (ServerWebSocket socket : sockets) {
                        if (socket.isClosed()) {
                            socket.close();
                            closedSockets.add(socket);
                        } else
                            socket.writeTextMessage(sensorData.toString());
                    }
                    for (ServerWebSocket socket : closedSockets)
                        this.sockets.remove(socket);

                    closedSockets.clear();

                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                //TODO
            }
        });
    }
    
}
