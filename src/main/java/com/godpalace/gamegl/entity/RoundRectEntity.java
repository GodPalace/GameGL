package com.godpalace.gamegl.entity;

import java.awt.*;
import java.io.Serial;

public class RoundRectEntity extends Entity {
    @Serial
    private static final long serialVersionUID = 3330000000000000004L;

    protected boolean isFill;
    protected int radius;

    public RoundRectEntity(int id, int width, int height) {
        this("RoundRectEntity", id, 0, 0, width, height);
    }

    public RoundRectEntity(String name, int id, int x, int y, int width, int height) {
        super(name, id, x, y, width, height);

        isFill = false;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setFill(boolean isFill) {
        this.isFill = isFill;
    }

    public boolean isFill() {
        return isFill;
    }

    @Override
    public void update(Graphics g) {
        if (isFill) {
            g.fillRoundRect(x, y, width, height, radius, radius);
        } else {
            g.drawRoundRect(x, y, width, height, radius, radius);
        }
    }
}
