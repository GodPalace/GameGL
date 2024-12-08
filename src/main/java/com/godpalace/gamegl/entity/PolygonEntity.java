package com.godpalace.gamegl.entity;

import java.awt.*;

public class PolygonEntity extends Entity {
    protected Polygon polygon;

    public PolygonEntity(Polygon polygon, int id, int x, int y) {
        this("PolygonEntity", id, polygon,
                x, y, polygon.getBounds().width, polygon.getBounds().height);
    }

    public PolygonEntity(String name, int id, Polygon polygon, int x, int y, int width, int height) {
        super(name, id, x, y, width, height);

        this.polygon = polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    @Override
    public void setEntityX(int x) {
        polygon.translate(x - this.x, 0);
        super.setEntityX(x);
    }

    @Override
    public void setEntityY(int y) {
        polygon.translate(0, y - this.y);
        super.setEntityY(y);
    }

    @Override
    public void update(Graphics g) {
        g.drawPolygon(polygon);
    }
}
