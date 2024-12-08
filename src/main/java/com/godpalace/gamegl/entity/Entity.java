package com.godpalace.gamegl.entity;

import com.godpalace.gamegl.entity.attribute.EntityAttribute;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogic;
import com.godpalace.gamegl.entity.logic.EntityLoopLogic;
import com.godpalace.gamegl.entity.logic.EntityMouseLogic;
import com.godpalace.gamegl.entity.logic.EntityRadioLogic;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Entity {
    protected int x, y, width, height, id, nameSpacing;
    protected String name;
    protected Color nameColor, color;
    protected Font nameFont;
    protected NamePosition namePosition;
    protected boolean isShowName, isShowEntity;

    protected EntityKeyboardLogic keyboardLogic;
    protected EntityMouseLogic mouseLogic;
    protected EntityLoopLogic loopLogic;
    protected EntityRadioLogic radioLogic;

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

        this.keyboardLogic = null;
        this.mouseLogic = null;
        this.loopLogic = null;
        this.radioLogic = null;

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

    public void setEntityKeyboardLogic(EntityKeyboardLogic logic) {
        this.keyboardLogic = logic;
    }

    public void doFiredKeyboardEvent(EntityKeyboardLogic.LogicType type, int key) {
        if (keyboardLogic == null)
            throw new RuntimeException("EntityKeyboardLogic is not set.");

        switch (type) {
            case DOWN -> keyboardLogic.onKeyDown(key);
            case UP -> keyboardLogic.onKeyUp(key);
            case TYPE -> keyboardLogic.onKeyTyped(key);
        }
    }

    public void setEntityMouseLogic(EntityMouseLogic logic) {
        this.mouseLogic = logic;
    }

    public void doFiredMouseEvent(EntityMouseLogic.LogicType type, int button, int x, int y) {
        if (mouseLogic == null)
            throw new RuntimeException("EntityMouseLogic is not set.");

        switch (type) {
            case DOWN -> mouseLogic.onMouseDown(button, x, y);
            case UP -> mouseLogic.onMouseUp(button, x, y);
            case CLICK -> mouseLogic.onMouseClick(button, x, y);
        }
    }

    public void setEntityLoopLogic(EntityLoopLogic logic) {
        this.loopLogic = logic;
    }

    public void setEntityRadioLogic(EntityRadioLogic logic) {
        this.radioLogic = logic;
    }

    public void moveEntity(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void moveEntityTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setEntityAttribute(String name, EntityAttribute<?> attribute) {
        this.attributes.put(name, attribute);
    }

    public EntityAttribute<?> getEntityAttribute(String name) {
        if (!this.attributes.containsKey(name))
            throw new RuntimeException("Attribute " + name + " not found.");

        return this.attributes.get(name);
    }

    public void removeEntityAttribute(String name) {
        this.attributes.remove(name);
    }

    public int getEntityAttributeCount() {
        return this.attributes.size();
    }

    public boolean hasEntityAttribute(String name) {
        return this.attributes.containsKey(name);
    }

    public void clearEntityAttributes() {
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
