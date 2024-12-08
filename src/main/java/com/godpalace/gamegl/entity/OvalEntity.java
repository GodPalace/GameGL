package com.godpalace.gamegl.entity;

import java.awt.*;

public class OvalEntity extends Entity {
    protected boolean isFill;

    public OvalEntity(int id, int width, int height) {
        this("OvalEntity", id, 0, 0, width, height);
    }

    public OvalEntity(String name, int id, int x, int y, int width, int height) {
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
            g.fillOval(x, y, width, height);
        } else {
            g.drawOval(x, y, width, height);
        }
    }
}
