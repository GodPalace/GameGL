package com.godpalace.gamegl.network.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

public class SyncObjectServer extends SyncServer {
    private final Server server;
    private final CopyOnWriteArrayList<WebSocket> clients;

    public SyncObjectServer(InetSocketAddress address,
                            String password, String key) {
        super(password, key);
        clients = new CopyOnWriteArrayList<>();

        server = new Server(address);
    }

    @Override
    public void start() {
        server.start();
    }

    @Override
    public void close() throws Exception {
        server.stop();
    }

    @Override
    public void onClientChange(WebSocket webSocket, byte[] data) {
        for (WebSocket client : clients) {
            if (webSocket.equals(client)) continue;
            client.send(packet(data, key));
        }
    }

    private class Server extends WebSocketServer {
        public Server(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        }

        @Override
        public void onClose(WebSocket webSocket, int i, String s, boolean b) {
            clients.remove(webSocket);
        }

        @Override
        public void onMessage(WebSocket webSocket, String s) {
        }

        @Override
        public void onMessage(WebSocket conn, ByteBuffer message) {
            byte[] data = unPacket(message.array(), unKey);
            String[] str = new String(data).split("\n");

            switch (str[0]) {
                case "JOIN" -> {
                    if (str.length == 2) {
                        if (str[1].equals(password)) {
                            clients.add(conn);
                            conn.send(packet("JOIN_SUCCESS".getBytes(), key));
                        } else {
                            conn.send(packet("JOIN_FAILURE".getBytes(), key));
                        }
                    }
                }

                case "SYNC_OBJECT" -> {
                    try {
                        onClientChange(conn, data);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            System.out.println(str[0]);
        }

        @Override
        public void onError(WebSocket webSocket, Exception e) {
            e.printStackTrace();
        }
    }
}
