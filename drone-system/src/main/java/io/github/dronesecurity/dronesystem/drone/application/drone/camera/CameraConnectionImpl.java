/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.camera;

import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.RawCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraConnection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Implementation of {@link CameraConnection} that manages the connection with the camera sensor.
 */
public class CameraConnectionImpl implements CameraConnection {

    private static final int EMPTY_IMAGE = 0;
    private static final int CONNECTION_PORT = 10_000;
    private Socket socket = new Socket();
    private InputStream inputStream;

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(" -> new")
    public @NotNull RawCameraData readCameraData() {
        try {
            if (this.tryConnect() && this.inputStream.available() > 0) {
                final ByteBuffer buffer = ByteBuffer.wrap(this.inputStream.readNBytes(Integer.BYTES))
                        .order(ByteOrder.LITTLE_ENDIAN);
                final int length = buffer.getInt();
                final byte[] image = this.inputStream.readNBytes(length);
                return new RawCameraData(image.length);
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(CameraConnectionImpl.class).error("Cannot read image from camera", e);
        }
        return new RawCameraData(EMPTY_IMAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        try {
            if (this.inputStream != null)
                this.inputStream.close();
            if (this.socket.isConnected())
                this.socket.close();
        } catch (IOException e) {
            LoggerFactory.getLogger(CameraConnectionImpl.class).error("Error closing connection", e);
        }
    }

    private boolean tryConnect() {
        if (!this.socket.isConnected()) {
            try {
                this.socket.connect(new InetSocketAddress("localhost", CONNECTION_PORT));
                this.inputStream = this.socket.getInputStream();
            } catch (IOException e) {
                this.socket = new Socket();
            }
        }
        return this.socket.isConnected();
    }
}
