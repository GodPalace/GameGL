package com.godpalace.gamegl.network.server;

import com.godpalace.gamegl.network.User;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserManageServer implements Closeable, AutoCloseable {
    private final ServerSocket server;
    private final CopyOnWriteArrayList<User> users;
    private final Thread thread;
    private final String key;

    public UserManageServer(ServerSocket server, String key) {
        this.server = server;
        this.users = new CopyOnWriteArrayList<>();
        this.thread = new Thread(new UserManageThread());
        this.key = new StringBuilder(key).reverse().toString();
    }

    public void startupServer() {
    }

    @Override
    public void close() throws IOException {
        server.close();
    }

    private class UserManageThread implements Runnable {
        @Override
        public void run() {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                while (true) {
                    try {
                        Socket socket = server.accept();
                        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());

                        byte[] buffer = new byte[1024];
                        int len;

                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }

                        String content = new String(unPacket(out.toByteArray(), key));
                        String[] split = content.split("\n");

                        switch (split[0]) {
                            case "QUERY" -> {
                            }
                            case "QUERY-PASSWORD" -> {
                            }
                            case "QUERY-EMAIL" -> {
                            }

                            case "REGISTER" -> {
                            }  // 注册普通用户
                            case "LOGIN" -> {
                            }
                            case "LOGOUT" -> {
                            }

                            case "CHANGE-USERNAME" -> {
                            }
                            case "CHANGE-PASSWORD" -> {
                            }
                            case "CHANGE-EMAIL" -> {
                            }

                            // root commands
                            case "ROOT-REGISTER" -> {
                            }  // 注册管理员用户

                            case "ROOT-QUERY" -> {
                            }
                            case "ROOT-QUERY-PASSWORD" -> {
                            }
                            case "ROOT-QUERY-EMAIL" -> {
                            }

                            case "ROOT-REMOVE-USER" -> {
                            }
                        }

                        in.close();
                        socket.close();
                        out.reset();
                    } catch (IOException e) {
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        static byte[] unPacket(byte[] packet, String key) {
            for (int i = 0; i < key.length(); i++) {
                for (int j = 0; j < packet.length; j++) {
                    packet[j] ^= (byte) key.charAt(i);
                }
            }

            return packet;
        }
    }
}
