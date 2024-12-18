package com.godpalace.gamegl.entity.attribute;

import com.godpalace.gamegl.entity.Entity;

import java.awt.*;

public class EntityIdAttribute extends EntityAttribute<Integer> {
    public EntityIdAttribute(Integer value) {
        super(value);
    }

    public int getId() {
        return getValue();
    }

    public void setId(int id) {
        setValue(id);
    }

    @Override
    public void update(Graphics g, Entity entity) {
        g.drawString("ID: " + getId(),
                entity.getEntityX() + entity.getEntityWidth() + 5,
                entity.getEntityY() + entity.getEntityHeight() / 2);
    }
}
