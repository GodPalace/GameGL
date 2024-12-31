package com.godpalace.gamegl.desktop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DesktopGraphics {
    private static native void DrawLine(int x1, int y1, int x2, int y2);
    private static native void DrawText(String str, int x, int y, int width, int height);
    private static native void DrawRect(int x, int y, int width, int height);
    private static native void DrawOval(int x, int y, int width, int height);

    static {
        File file = new File("GameGLDll.dll");

        if (!file.exists()) {
            try {
                URL url = DesktopGraphics.class.getResource("/dll/GameGLDll.dll");
                if (url == null) throw new RuntimeException("Failed to find GameGLDll.dll");

                InputStream in = url.openStream();
                FileOutputStream out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len;

                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }

                in.close();
                out.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to load GameGLDll.dll", e);
            }
        }

        System.load(file.getAbsolutePath());
    }

    private DesktopGraphics() {
    }

    public static void drawLine(int x1, int y1, int x2, int y2) {
        DrawLine(x1, y1, x2, y2);
    }

    public static void drawText(String str, int x, int y, int width, int height) {
        DrawText(str, x, y, width, height);
    }

    public static void drawText(String str, int x, int y) {
        int width = str.length() * 8;
        int height = 18;

        DesktopGraphics.drawText(str, x, y, width, height);
    }

    public static void drawRect(int x, int y, int width, int height) {
        DrawRect(x, y, width, height);
    }

    public static void drawOval(int x, int y, int width, int height) {
        DrawOval(x, y, width, height);
    }
}
