package com.godpalace.gamegl.test;

import com.godpalace.gamegl.desktop.DesktopGraphics;

import java.awt.*;

public class DrawTest {
    public static void main(String[] args) {
        DesktopGraphics g = DesktopGraphics.createDesktopGraphics();
        g.setColor(Color.RED);
        g.setSize(5);
    }
}
