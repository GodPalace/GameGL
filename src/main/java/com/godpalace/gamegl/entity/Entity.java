package com.godpalace.gamegl.entity;

import com.godpalace.gamegl.engine.EntityPhysicsEngine;
import com.godpalace.gamegl.entity.attribute.EntityAttribute;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogic;
import com.godpalace.gamegl.entity.logic.EntityLoopLogic;
import com.godpalace.gamegl.entity.logic.EntityMouseLogic;
import com.godpalace.gamegl.entity.logic.EntityRadioLogic;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3330000000000000000L;

    private final Object physicsLock = new Object();

    protected int x, y, width, height, id, nameSpacing;
    protected String name;
    protected Color nameColor, color;
    protected Font nameFont;
    protected NamePosition namePosition;
    protected boolean isShowName, isShowEntity, isEnabledPhysics;

    protected final CopyOnWriteArrayList<EntityKeyboardLogic> keyboardLogics;
    protected final CopyOnWriteArrayList<EntityMouseLogic> mouseLogics;
    protected final CopyOnWriteArrayList<EntityLoopLogic> loopLogics;
    protected final CopyOnWriteArrayList<EntityRadioLogic> radioLogics;
    protected final CopyOnWriteArrayList<EntityPhysicsEngine> engines;
    protected final ConcurrentHashMap<String, EntityAttribute<?>> attributes;

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
        this.isEnabledPhysics = true;
        this.color = Color.BLACK;

        this.keyboardLogics = new CopyOnWriteArrayList<>();
        this.mouseLogics = new CopyOnWriteArrayList<>();
        this.loopLogics = new CopyOnWriteArrayList<>();
        this.radioLogics = new CopyOnWriteArrayList<>();
        this.engines = new CopyOnWriteArrayList<>();

        this.attributes = new ConcurrentHashMap<>();

        new Thread(this::updateEntityPhysics).start();
    }

    public void addEntityPhysicsEngine(EntityPhysicsEngine engine) {
        boolean isEmpty = engines.isEmpty();
        engines.add(engine);
        engine.setEntity(this);

        if (isEmpty) {
            synchronized (physicsLock) {
                physicsLock.notifyAll();
            }
        }
    }

    public void removeEntityPhysicsEngine(EntityPhysicsEngine engine) {
        engines.remove(engine);
    }

    public void enablePhysics() {
        isEnabledPhysics = true;
    }

    public void disablePhysics() {
        isEnabledPhysics = false;

        synchronized (physicsLock) {
            physicsLock.notifyAll();
        }
    }

    private void updateEntityPhysics() {
        while (true) {
            try {
                if (engines.isEmpty() || !isEnabledPhysics) {
                    synchronized (physicsLock) {
                        physicsLock.wait();
                    }
                }

                for (EntityPhysicsEngine engine : engines) {
                    if (engine.isStarted()) {
                        engine.update(System.currentTimeMillis() - engine.getStartTime());
                    }
                }

                synchronized (this) {
                    wait(10);
                }
            } catch (Exception e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
                break;
            }
        }
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

    public synchronized void doFiredMouseEvent(
            EntityMouseLogic.LogicType type, int button, int count, int x, int y) {
        if (mouseLogics.isEmpty())
            throw new RuntimeException("EntityMouseLogic is empty.");

        for (EntityMouseLogic mouseLogic : mouseLogics) {
            switch (type) {
                case DOWN -> mouseLogic.onMouseDown(button, count, x, y);
                case UP -> mouseLogic.onMouseUp(button, count, x, y);
                case CLICK -> mouseLogic.onMouseClick(button, count, x, y);
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

    public enum NamePosition implements Serializable {
        TOP, BOTTOM, LEFT, RIGHT
    }
}
