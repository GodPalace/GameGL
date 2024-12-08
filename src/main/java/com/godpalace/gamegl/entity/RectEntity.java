package com.godpalace.gamegl.entity;

import java.awt.*;

public class RectEntity extends Entity {
    protected boolean isFill;

    public RectEntity(int id, int width, int height) {
        this("RectEntity", id, 0, 0, width, height);
    }

    public RectEntity(String name, int id, int x, int y, int width, int height) {
        super(name, id, x, y, width, height);

        isFill = false;
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
            g.fillRect(x, y, width, height);
        } else {
            g.drawRect(x, y, width, height);
        }
    }
}
