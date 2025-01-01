package com.godpalace.gamegl.desktop;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DesktopGraphics {
    private Color color = Color.BLACK;
    private int size = 3;

    private native void Init();
    private native void Dispose();

    private native void SetColor(int r, int g, int b);
    private native void SetSize(int size);

    private native void DrawLine(int x1, int y1, int x2, int y2);
    private native void DrawText(String str, int x, int y);
    private native void DrawRect(int x, int y, int width, int height);
    private native void DrawOval(int x, int y, int width, int height);

    static {
        File file = new File("GameGLDll.dll");

        try {
            URL url = DesktopGraphics.class.getResource("/dll/GameGLDll.dll");
            if (url == null) throw new RuntimeException("Failed to find GameGLDll.dll");

            InputStream in = url.openStream();
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[10240];
            int len;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load GameGLDll.dll", e);
        }

        System.load(file.getAbsolutePath());
    }

    private DesktopGraphics() {
        SetColor(color.getRed(), color.getGreen(), color.getBlue());
        SetSize(size);
    }

    public static DesktopGraphics createDesktopGraphics() {
        DesktopGraphics graphics = new DesktopGraphics();
        graphics.Init();
        return graphics;
    }

    public void dispose() {
        Dispose();
    }

    public void setColor(Color color) {
        this.color = color;
        SetColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public Color getColor() {
        return color;
    }

    public void setSize(int size) {
        this.size = size;
        SetSize(size);
    }

    public int getSize() {
        return size;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        DrawLine(x1, y1, x2, y2);
    }

    public void drawText(String str, int x, int y) {
        DrawText(str, x, y);
    }

    public void drawRect(int x, int y, int width, int height) {
        DrawRect(x, y, width, height);
    }

    public void drawOval(int x, int y, int width, int height) {
        DrawOval(x, y, width, height);
    }
}
