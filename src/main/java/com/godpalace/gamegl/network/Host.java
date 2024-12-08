package com.godpalace.gamegl.network;

import java.net.InetAddress;

public class Host {
    public static boolean isOnline(String ip, int timeout) {
        try {
            return InetAddress.getByName(ip).isReachable(timeout);
        } catch (Exception e) {
            return false;
        }
    }
}
