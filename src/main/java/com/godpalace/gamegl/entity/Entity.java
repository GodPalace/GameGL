package com.godpalace.gamegl.entity;

import com.godpalace.gamegl.entity.attribute.EntityAttribute;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogic;
import com.godpalace.gamegl.entity.logic.EntityLoopLogic;
import com.godpalace.gamegl.entity.logic.EntityMouseLogic;
import com.godpalace.gamegl.entity.logic.EntityRadioLogic;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Entity {
    protected int x, y, width, height, id, nameSpacing;
    protected String name;
    protected Color nameColor, color;
    protected Font nameFont;
    protected NamePosition namePosition;
    protected boolean isShowName, isShowEntity;

    protected ArrayList<EntityKeyboardLogic> keyboardLogics;
    protected ArrayList<EntityMouseLogic> mouseLogics;
    protected ArrayList<EntityLoopLogic> loopLogics;
    protected ArrayList<EntityRadioLogic> radioLogics;

    protected ConcurrentHashMap<String, EntityAttribute<?>> attributes;

    public Entity(String name, int id, int x, int y, int width, int height) {
        this.name = name;
        this.id = id;
        this.nameColor = Color.BLACK;
        this.nameFont = new Font("Arial", Font.PLAIN, 12);
        this.namePosition = NamePosition.TOP;
        this.nameSpacing = 5;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isShowName = false;
        this.isShowEntity = true;
        this.color = Color.BLACK;

        this.keyboardLogics = new ArrayList<>();
        this.mouseLogics = new ArrayList<>();
        this.loopLogics = new ArrayList<>();
        this.radioLogics = new ArrayList<>();

        this.attributes = new ConcurrentHashMap<>();
    }

    public void setEntityColor(Color color) {
        this.color = color;
    }

    public Color getEntityColor() {
        return color;
    }

    public void setEntityX(int x) {
        this.x = x;
    }

    public void setEntityY(int y) {
        this.y = y;
    }

    public int getEntityX() {
        return x;
    }

    public int getEntityY() {
        return y;
    }

    public void setEntityWidth(int width) {
        this.width = width;
    }

    public void setEntityHeight(int height) {
        this.height = height;
    }

    public int getEntityWidth() {
        return width;
    }

    public int getEntityHeight() {
        return height;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setEntityName(String name) {
        this.name = name;
    }

    public String getEntityName() {
        return name;
    }

    public void setEntityNameColor(Color color) {
        this.nameColor = color;
    }

    public Color getEntityNameColor() {
        return nameColor;
    }

    public void setEntityNameFont(Font font) {
        this.nameFont = font;
    }

    public Font getEntityNameFont() {
        return nameFont;
    }

    public void setEntityNamePosition(NamePosition position) {
        this.namePosition = position;
    }

    public NamePosition getEntityNamePosition() {
        return namePosition;
    }

    public void setNameSpacing(int spacing) {
        this.nameSpacing = spacing;
    }

    public int getNameSpacing() {
        return nameSpacing;
    }

    public void setShowName(boolean b) {
        this.isShowName = b;
    }

    public synchronized void addEntityKeyboardLogic(EntityKeyboardLogic logic) {
        this.keyboardLogics.add(logic);
    }

    public synchronized void removeEntityKeyboardLogic(EntityKeyboardLogic logic) {
        this.keyboardLogics.remove(logic);
    }

    public synchronized void doFiredKeyboardEvent(EntityKeyboardLogic.LogicType type, int key) {
        if (keyboardLogics.isEmpty())
            throw new RuntimeException("EntityKeyboardLogic is empty.");

        for (EntityKeyboardLogic keyboardLogic : keyboardLogics) {
            switch (type) {
                case DOWN -> keyboardLogic.onKeyDown(key);
                case UP -> keyboardLogic.onKeyUp(key);
                case DOWNING -> keyboardLogic.onKeyDowning(key);
            }
        }
    }

    public synchronized void addEntityMouseLogic(EntityMouseLogic logic) {
        this.mouseLogics.add(logic);
    }

    public synchronized void removeEntityMouseLogic(EntityMouseLogic logic) {
        this.mouseLogics.remove(logic);
    }

    public synchronized void doFiredMouseEvent(EntityMouseLogic.LogicType type, int button, int x, int y) {
        if (mouseLogics.isEmpty())
            throw new RuntimeException("EntityMouseLogic is empty.");

        for (EntityMouseLogic mouseLogic : mouseLogics) {
            switch (type) {
                case DOWN -> mouseLogic.onMouseDown(button, x, y);
                case UP -> mouseLogic.onMouseUp(button, x, y);
                case CLICK -> mouseLogic.onMouseClick(button, x, y);
            }
        }
    }

    public synchronized void addEntityLoopLogic(EntityLoopLogic logic) {
        this.loopLogics.add(logic);
    }

    public synchronized void removeEntityLoopLogic(EntityLoopLogic logic) {
        this.loopLogics.remove(logic);
    }

    public synchronized void addEntityRadioLogic(EntityRadioLogic logic) {
        this.radioLogics.add(logic);
    }

    public synchronized void removeEntityRadioLogic(EntityRadioLogic logic) {
        this.radioLogics.remove(logic);
    }

    public void moveEntity(int dx, int dy) {
        this.setEntityX(this.getEntityX() + dx);
        this.setEntityY(this.getEntityY() + dy);
    }

    public void moveEntityTo(int x, int y) {
        this.setEntityX(x);
        this.setEntityY(y);
    }

    public synchronized void setEntityAttribute(String name, EntityAttribute<?> attribute) {
        this.attributes.put(name, attribute);
    }

    public EntityAttribute<?> getEntityAttribute(String name) {
        if (!this.attributes.containsKey(name))
            throw new RuntimeException("Attribute " + name + " not found.");

        return this.attributes.get(name);
    }

    public synchronized void removeEntityAttribute(String name) {
        this.attributes.remove(name);
    }

    public synchronized int getEntityAttributeCount() {
        return this.attributes.size();
    }

    public synchronized boolean hasEntityAttribute(String name) {
        return this.attributes.containsKey(name);
    }

    public synchronized void clearEntityAttributes() {
        this.attributes.clear();
    }

    public void show() {
        this.isShowEntity = true;
    }

    public void hide() {
        this.isShowEntity = false;
    }

    public boolean isShow() {
        return this.isShowEntity;
    }

    public abstract void update(Graphics g);

    public void drawEntityAttributes(Graphics g) {
        for (EntityAttribute<?> attribute : attributes.values()) {
            attribute.update(g, this);
        }
    }

    public void drawEntityName(Graphics g) {
        if (this.isShowName) {
            g.setColor(nameColor);
            g.setFont(nameFont);

            Rectangle2D size = g.getFontMetrics().getStringBounds(name, g);
            int nameWidth = (int) size.getWidth();
            int nameHeight = (int) size.getHeight();

            switch (namePosition) {
                case TOP -> g.drawString(
                        name, x + width / 2 - nameWidth / 2, y - nameHeight + nameSpacing);

                case BOTTOM -> g.drawString(
                        name, x + width / 2 - nameWidth / 2, y + height + nameHeight + nameSpacing);

                case LEFT -> g.drawString(
                        name, x - nameWidth - nameSpacing, y + height / 2 + nameHeight / 2);

                case RIGHT -> g.drawString(
                        name, x + width + nameSpacing, y + height / 2 + nameHeight / 2);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entity && ((Entity) obj).getId() == this.getId();
    }

    public enum NamePosition {
        TOP, BOTTOM, LEFT, RIGHT
    }
}
