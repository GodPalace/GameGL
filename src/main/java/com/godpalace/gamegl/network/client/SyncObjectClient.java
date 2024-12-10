package com.godpalace.gamegl.network.client;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;

public class SyncObjectClient<T extends Serializable> extends SyncClient {
    private final SyncObject<T> object;
    private final Client client;
    private boolean isJoined;

    public SyncObjectClient(URI serverUri, SyncObject<T> object,
                            String password, String key) {
        super(password, key);
        this.isJoined = false;

        this.object = object;
        this.object.setSyncer(this);

        this.client = new Client(serverUri);
        this.client.setTcpNoDelay(true);
    }

    @Override
    public void connect() {
        client.connect();
    }

    @Override
    public void disconnect() {
        client.close();
    }

    @Override
    public void changeObject(Object object) throws IOException {
        if (client.isClosed())
            throw new IOException("Connection closed");

        if (isJoined) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            bout.write("SYNC_OBJECT\n".getBytes());

            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(object);
            out.flush();
            out.close();

            client.send(ByteBuffer.wrap(packet(bout.toByteArray(), key)));
            bout.close();
        }
    }

    @Override
    public void onRemoteChange(Object object) {
        this.object.setObjectNotSync((T) object);
    }

    private class Client extends WebSocketClient {
        public Client(URI serverUri) {
            super(serverUri);

            this.setConnectionLostTimeout(5000);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            send(ByteBuffer.wrap(packet(("JOIN\n" + password).getBytes(), key)));
        }

        @Override
        public void onMessage(String s) {
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            byte[] data = unPacket(bytes.array(), unKey);
            String[] str = new String(data).split("\n");

            switch (str[0]) {
                case "JOIN_SUCCESS" -> isJoined = true;
                case "JOIN_FAILURE" -> {
                    isJoined = false;
                    throw new RuntimeException("Wrong password");
                }

                case "SYNC_OBJECT" -> {
                    try {
                        byte[] objData = new byte[bytes.remaining() - 12];
                        System.arraycopy(bytes.array(), 12, objData, 0, objData.length);

                        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(objData));
                        T obj = (T) in.readObject();

                        in.close();
                        onRemoteChange(obj);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        @Override
        public void onClose(int i, String s, boolean b) {
        }

        @Override
        public void onError(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
