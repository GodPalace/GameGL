package com.godpalace.gamegl.component;

import com.godpalace.gamegl.entity.EntityPane;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class SelectBoxPane extends EntityPane implements MouseMotionListener {
    private boolean isDraw;
    private int x, y, r, width, height;
    private Color borderColor;

    public SelectBoxPane(int width, int height) {
        this.width = width;
        this.height = height;
        this.isDraw = false;

        this.addMouseMotionListener(this);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isDraw = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isDraw = false;
                repaint();
            }
        });

        this.borderColor = Color.BLACK;
        this.r = 0;
    }

    public void setSelectionWidth(int width) {
        this.width = width;
    }

    public void setSelectionHeight(int height) {
        this.height = height;
    }

    public int getSelectionWidth() {
        return this.width;
    }

    public int getSelectionHeight() {
        return this.height;
    }

    public void setRound(int r) {
        this.r = r;
    }

    public int getRound() {
        return this.r;
    }

    public void setColor(Color color) {
        this.borderColor = color;
    }

    public Color getColor() {
        return this.borderColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isDraw) {
            g.setColor(borderColor);
            g.drawRoundRect(x, y, width, height, r, r);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = (e.getX() / width) * width;
        y = (e.getY() / height) * height;

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = (e.getX() / width) * height;
        y = (e.getY() / width) * height;

        repaint();
    }
}
