package com.godpalace.gamegl.network.client;

import java.io.IOException;
import java.io.Serializable;

public final class SyncObject<T extends Serializable> implements Serializable {
    private T object;
    private SyncClient syncClient;

    public SyncObject(T object) {
        this.object = object;
        this.syncClient = null;
    }

    public synchronized void setSyncer(SyncClient syncClient) {
        this.syncClient = syncClient;
    }

    public synchronized SyncClient getSyncer() {
        return this.syncClient;
    }

    synchronized void setObjectNotSync(T object) {
        this.object = object;
    }

    public synchronized void setObject(T object) {
        this.object = object;

        if (this.syncClient != null) {
            try {
                syncClient.changeObject(object);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized T getObject() {
        return this.object;
    }
}
