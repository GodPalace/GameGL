package com.godpalace.gamegl.entity.attribute;

import com.godpalace.gamegl.entity.Entity;

import java.awt.*;

public class EntityHealthAttribute extends EntityAttribute<Integer> {
    protected int maxHealth;

    public EntityHealthAttribute(int maxHealth, int value) {
        super(value);
        this.setMaxHealth(maxHealth);
    }

    @Override
    public void setValue(Integer value) {
        if (value > maxHealth || value < 0)
            throw new IllegalArgumentException("Value cannot be greater than max health.");

        super.setValue(value);
    }

    public void setMaxHealth(int maxHealth) {
        if (maxHealth < 0)
            throw new IllegalArgumentException("Max health cannot be negative.");

        this.maxHealth = maxHealth;
        if (value > maxHealth) value = maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void damage(int damage) {
        if (damage < 0)
            throw new IllegalArgumentException("Damage cannot be negative.");

        value -= damage;
        if (value < 0) value = 0;
    }

    public void heal(int heal) {
        if (heal < 0)
            throw new IllegalArgumentException("Heal cannot be negative.");

        value += heal;
        if (value > maxHealth) value = maxHealth;
    }

    public boolean isDead() {
        return value <= 0;
    }

    @Override
    public void update(Graphics g, Entity entity) {
        g.setColor(Color.BLACK);
        g.drawRect(entity.getEntityX() + entity.getEntityWidth() / 2 - 50,
                   entity.getEntityY() - 15,
                   100, 10);

        g.setColor(Color.RED);
        g.fillRect(entity.getEntityX() + entity.getEntityWidth() / 2 - 49,
                   entity.getEntityY() - 14,
                      (int) ((float) value / (float) maxHealth * 100) - 1, 9);
    }

    @Override
    public String toString() {
        return "Health: " + value;
    }
}
