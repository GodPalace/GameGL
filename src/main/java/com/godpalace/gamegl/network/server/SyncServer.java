package com.godpalace.gamegl.network.server;

import org.java_websocket.WebSocket;

import java.io.IOException;

public abstract class SyncServer {
    protected final String password, key, unKey;

    public SyncServer(String password, String key) {
        this.password = password;
        this.key = key;
        this.unKey = new StringBuilder(key).reverse().toString();
    }

    protected byte[] packet(byte[] data, String key) {
        byte[] keyBytes = key.getBytes();

        for (byte keyByte : keyBytes) {
            for (int j = 0; j < data.length; j++) {
                data[j] ^= keyByte;
            }
        }

        return data;
    }

    protected byte[] unPacket(byte[] data, String unKey) {
        byte[] keyBytes = unKey.getBytes();

        for (byte keyByte : keyBytes) {
            for (int j = 0; j < data.length; j++) {
                data[j] ^= keyByte;
            }
        }

        return data;
    }

    public abstract void start() throws IOException;
    public abstract void close() throws Exception;
    public abstract void onClientChange(WebSocket client, byte[] data);
}
