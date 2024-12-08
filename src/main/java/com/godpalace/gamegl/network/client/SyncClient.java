package com.godpalace.gamegl.network.client;

import java.io.IOException;

public abstract class SyncClient {
    protected final String password, key, unKey;

    public SyncClient(String password, String key) {
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

    public abstract void connect() throws IOException;
    public abstract void disconnect() throws IOException;
    public abstract void changeObject(Object object) throws IOException;
    public abstract void onRemoteChange(Object object) throws IOException;
}
