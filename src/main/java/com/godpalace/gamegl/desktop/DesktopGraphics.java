package com.godpalace.gamegl.desktop;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DesktopGraphics {
    private static final DesktopGraphics INSTANCE;

    private static final File file = new File("GameGLDll.dll");

    private Color color = Color.BLACK;
    private int size = 3;
    private boolean isDisposed = false;

    private native void Init();
    private native void Dispose();

    private native void SetColor(int r, int g, int b);
    private native void SetSize(int size);

    private native void DrawPixel(int x, int y);
    private native void DrawLine(int x1, int y1, int x2, int y2);
    private native void DrawText(String str, int x, int y);

    private native void DrawRect(int x, int y, int width, int height);
    private native void FillRect(int x, int y, int width, int height);

    private native void Flush();

    static {
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

            System.load(file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load GameGLDll.dll", e);
        }

        INSTANCE = new DesktopGraphics();
        INSTANCE.Init();
    }

    public static DesktopGraphics getInstance() {
        return INSTANCE;
    }

    public void dispose() {
        if (isDisposed) throw new RuntimeException("Graphics already disposed");

        isDisposed = true;
        Dispose();
    }

    public void setColor(Color color) {
        if (isDisposed) throw new RuntimeException("Graphics already disposed");
        this.color = color;
        SetColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public Color getColor() {
        return color;
    }

    public void setSize(int size) {
        if (isDisposed) throw new RuntimeException("Graphics already disposed");
        this.size = size;
        SetSize(size);
    }

    public int getSize() {
        return size;
    }

    public void drawPixel(int x, int y) {
        if (isDisposed) throw new RuntimeException("Graphics already disposed");
        DrawPixel(x, y);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        if (isDisposed) throw new RuntimeException("Graphics already disposed");
        DrawLine(x1, y1, x2, y2);
    }

    public void drawText(String str, int x, int y) {
        if (isDisposed) throw new RuntimeException("Graphics already disposed");
        DrawText(str, x, y);
    }

    public void drawRect(int x, int y, int width, int height) {
        if (isDisposed) throw new RuntimeException("Graphics already disposed");
        DrawRect(x, y, width, height);
    }

    public void fillRect(int x, int y, int width, int height) {
        if (isDisposed) throw new RuntimeException("Graphics already disposed");
        FillRect(x, y, width, height);
    }

    public void flush() {
        if (isDisposed) throw new RuntimeException("Graphics already disposed");
        Flush();
    }
}
