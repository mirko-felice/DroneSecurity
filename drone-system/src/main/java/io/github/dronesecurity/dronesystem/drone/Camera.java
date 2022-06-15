/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Item representing a real camera sensor and observing its values.
 */
public class Camera extends AbstractSensor<Byte[]> {

    private static final int CONNECTION_PORT = 10_000;

    private Socket socket;
    private InputStream inputStream;
    private byte[] image;

    /**
     * Instantiates the socket for the connection with its camera.
     */
    public Camera() {
        this.image = new byte[] {};
        this.socket = new Socket();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getScriptName() {
        return this.isRaspberry() ? "camera" : "cameraSimulator";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readData() {
        try {
            if (this.tryConnect() && this.inputStream.available() > 0) {
                final ByteBuffer buffer = ByteBuffer.wrap(this.inputStream.readNBytes(Integer.BYTES))
                        .order(ByteOrder.LITTLE_ENDIAN);
                final int length = buffer.getInt();
                this.image = this.inputStream.readNBytes(length);
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Cannot read data from sensor", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte[] getData() {
        final Byte[] objectImage = ArrayUtils.toObject(this.image);
        if (objectImage == null)
            return new Byte[] {};
        return objectImage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deactivate() {
        super.deactivate();
        try {
            if (this.inputStream != null)
                this.inputStream.close();
            if (this.socket.isConnected())
                this.socket.close();
        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("Error closing connection", e);
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
