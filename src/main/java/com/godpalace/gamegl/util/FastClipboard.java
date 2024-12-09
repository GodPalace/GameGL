package com.godpalace.gamegl.util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class FastClipboard {
    private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private FastClipboard() {}

    public static void setText(String text) {
        clipboard.setContents(new StringSelection(text), null);
    }

    public static String getText() {
        try {
            return (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean hasText() {
        return getText() != null;
    }

    public static void clear() {
        clipboard.setContents(new StringSelection(""), null);
    }
}
