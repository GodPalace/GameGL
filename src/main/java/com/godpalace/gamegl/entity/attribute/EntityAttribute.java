package com.godpalace.gamegl.entity.attribute;

import com.godpalace.gamegl.entity.Entity;

import java.awt.*;
import java.io.Serializable;

public class EntityAttribute<T extends Serializable> implements Serializable {
    protected T value;

    public EntityAttribute(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void update(Graphics g, Entity entity) {}
}
