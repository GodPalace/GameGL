package com.godpalace.gamegl.entity;

import com.godpalace.gamegl.entity.logic.EntityKeyboardLogic;
import com.godpalace.gamegl.entity.logic.EntityMouseLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

// 注意: 继承EntityPane类并重写paintComponent方法时, 必须调用super.paintComponent(g)方法
public class EntityPane extends JPanel implements KeyListener, MouseListener {
    private final ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<Entity>> entities;
    private final ConcurrentHashMap<Integer, Integer> idToLayers;
    protected Color backgroundColor;
    protected Thread loopThread;

    public EntityPane() {
        entities = new ConcurrentSkipListMap<>(Comparator.comparingInt(o -> o));
        idToLayers = new ConcurrentHashMap<>();
        backgroundColor = null;

        loopThread = new Thread(new LoopThread());
        loopThread.start();

        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseListener(this);
    }

    public final int getLayerCount() {
        return entities.size();
    }

    public final int getEntityCount() {
        return idToLayers.size();
    }

    public final int getEntityLayer(Entity entity) {
        return this.getEntityLayer(entity.getId());
    }

    public final int getEntityLayer(int id) {
        return idToLayers.get(id);
    }

    public final void addEntity(Entity entity) {
        this.addEntity(entity, 0);
    }

    public final void addEntity(Entity entity, int layer) {
        if (idToLayers.containsKey(entity.getId()))
            throw new IllegalArgumentException("Entity with id " + entity.getId() +
                    " already exists in this pane.");

        if (layer < 0)
            throw new IllegalArgumentException("Layer must be non-negative.");

        if (!entities.containsKey(layer))
            entities.put(layer, new CopyOnWriteArrayList<>());
        entities.get(layer).add(entity);
        idToLayers.put(entity.getId(), layer);

        synchronized (entities) {
            entities.notifyAll();
        }
    }

    public final void removeEntity(Entity entity) {
        removeEntity(entity.getId());
    }

    public final void removeEntity(int id) {
        int layer = idToLayers.get(id);

        entities.get(layer).removeIf(entity -> entity.getId() == id);
        idToLayers.remove(id);

        if (entities.get(layer).isEmpty()) {
            entities.remove(layer);
        }

        repaint();
    }

    public boolean isEntityInPane(int id) {
        return entities.containsKey(id);
    }

    public final Entity getEntity(int id) {
        if (!idToLayers.containsKey(id))
            throw new IllegalArgumentException(
                    "Entity with id " + id + " does not exist in this pane.");

        for (Entity entity : entities.get(idToLayers.get(id))) {
            if (entity.getId() == id) {
                return entity;
            }
        }

        return null;
    }

    public final boolean hasEntities() {
        return !idToLayers.isEmpty();
    }

    public boolean isEntityHit(Entity entity1, Entity entity2) {
        return this.isEntityHit(entity1.getId(), entity2.getId());
    }

    public boolean isEntityHit(int id, int id2) {
        Entity entity1 = getEntity(id);
        Entity entity2 = getEntity(id2);

        int x1 = entity1.getEntityX();
        int y1 = entity1.getEntityY();
        int w1 = entity1.getEntityWidth();
        int h1 = entity1.getEntityHeight();

        int x2 = entity2.getEntityX();
        int y2 = entity2.getEntityY();
        int w2 = entity2.getEntityWidth();
        int h2 = entity2.getEntityHeight();

        return x1 + w1 >= x2 && x2 + w2 >= x1 && y1 + h1 >= y2 && y2 + h2 >= y1;
    }

    public Edge getEntityHitEdge(Entity entity) {
        int x = entity.getEntityX();
        int y = entity.getEntityY();
        int w = entity.getEntityWidth();
        int h = entity.getEntityHeight();

        if (x <= 0) {
            if (y <= 0) {
                return Edge.TOP_LEFT;
            } else if (y + h >= getHeight()) {
                return Edge.BOTTOM_LEFT;
            } else {
                return Edge.LEFT;
            }
        } else if (x + w >= getWidth()) {
            if (y <= 0) {
                return Edge.TOP_RIGHT;
            } else if (y + h >= getHeight()) {
                return Edge.BOTTOM_RIGHT;
            } else {
                return Edge.RIGHT;
            }
        } else {
            if (y <= 0) {
                return Edge.TOP;
            } else if (y + h >= getHeight()) {
                return Edge.BOTTOM;
            } else {
                return Edge.NONE;
            }
        }
    }

    public void sendRadio(String message) {
        for (CopyOnWriteArrayList<Entity> layer : entities.values()) {
            for (Entity entity : layer) {
                if (entity.radioLogic != null) {
                    entity.radioLogic.onReceiveRadio(message);
                }
            }
        }
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 画背景色
        if (backgroundColor == null) {
            g.clearRect(0, 0, getWidth(), getHeight());
        } else {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // 画实体
        for (CopyOnWriteArrayList<Entity> layer : entities.values()) {
            for (Entity entity : layer) {
                if (entity.isShow()) {
                    g.setColor(entity.getEntityColor());

                    entity.update(g);
                    entity.drawEntityName(g);
                    entity.drawEntityAttributes(g);
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        for (CopyOnWriteArrayList<Entity> layer : entities.values()) {
            for (Entity entity : layer) {
                if (entity.keyboardLogic != null) {
                    entity.doFiredKeyboardEvent(EntityKeyboardLogic.LogicType.TYPE, e.getKeyCode());
                }
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (CopyOnWriteArrayList<Entity> layer : entities.values()) {
            for (Entity entity : layer) {
                if (entity.keyboardLogic != null) {
                    entity.doFiredKeyboardEvent(EntityKeyboardLogic.LogicType.DOWN, e.getKeyCode());
                }
            }
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (CopyOnWriteArrayList<Entity> layer : entities.values()) {
            for (Entity entity : layer) {
                if (entity.keyboardLogic != null) {
                    entity.doFiredKeyboardEvent(EntityKeyboardLogic.LogicType.UP, e.getKeyCode());
                }
            }
        }

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (CopyOnWriteArrayList<Entity> layer : entities.values()) {
            for (Entity entity : layer) {
                if (x >= entity.getEntityX() && x <= entity.getEntityX() + entity.getEntityWidth() &&
                    y >= entity.getEntityY() && y <= entity.getEntityY() + entity.getEntityHeight()) {

                    if (entity.mouseLogic != null) {
                        entity.doFiredMouseEvent(EntityMouseLogic.LogicType.CLICK, e.getButton(),
                                x - entity.getEntityX(), y - entity.getEntityY());
                    }
                }
            }
        }

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        for (CopyOnWriteArrayList<Entity> layer : entities.values()) {
            for (Entity entity : layer) {
                if (x >= entity.getEntityX() && x <= entity.getEntityX() + entity.getEntityWidth() &&
                        y >= entity.getEntityY() && y <= entity.getEntityY() + entity.getEntityHeight()) {
                    if (entity.mouseLogic != null) {
                        entity.doFiredMouseEvent(EntityMouseLogic.LogicType.DOWN, e.getButton(),
                                x - entity.getEntityX(), y - entity.getEntityY());
                    }
                }
            }
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
         int x = e.getX();
         int y = e.getY();

         for (CopyOnWriteArrayList<Entity> layer : entities.values()) {
             for (Entity entity : layer) {
                 if (x >= entity.getEntityX() && x <= entity.getEntityX() + entity.getEntityWidth() &&
                         y >= entity.getEntityY() && y <= entity.getEntityY() + entity.getEntityHeight()) {
                     if (entity.mouseLogic != null) {
                         entity.doFiredMouseEvent(EntityMouseLogic.LogicType.UP, e.getButton(),
                                 x - entity.getEntityX(), y - entity.getEntityY());
                     }
                 }
             }
         }

         repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    protected class LoopThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    if (hasEntities()) {
                        long startTime = System.currentTimeMillis();

                        for (CopyOnWriteArrayList<Entity> layer : entities.values()) {
                            for (Entity entity : layer) {
                                if (entity.loopLogic != null) {
                                    entity.loopLogic.onLoop(System.currentTimeMillis()
                                            - startTime);
                                }
                            }
                        }

                        synchronized (entities) {
                            entities.wait(25);
                        }
                    } else {
                        synchronized (entities) {
                            entities.wait();
                        }
                    }

                    repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public enum Edge {
        TOP, BOTTOM, LEFT, RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, NONE
    }
}
