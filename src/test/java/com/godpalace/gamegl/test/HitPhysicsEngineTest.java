package com.godpalace.gamegl.test;

import com.godpalace.gamegl.entity.Entity;
import com.godpalace.gamegl.entity.EntityPane;
import com.godpalace.gamegl.entity.ImageEntity;
import com.godpalace.gamegl.entity.RectEntity;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogicAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class HitPhysicsEngineTest {
    public static EntityPane pane = new EntityPane();
    public static ImageEntity entity;
    public static final CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();
    public static boolean isJump = false;
    public static Color color;
    public static int speed = 3;

    public static boolean TouchTop(int range){
        for (Entity entity : entities) {
            EntityPane.ContactSurface contactSurface = pane.getEntityHitContactSurface(HitPhysicsEngineTest.entity, entity, range);
            if (contactSurface == EntityPane.ContactSurface.TOP || contactSurface == EntityPane.ContactSurface.TOP_LEFT || contactSurface == EntityPane.ContactSurface.TOP_RIGHT
                    && HitPhysicsEngineTest.entity.getEntityX() + HitPhysicsEngineTest.entity.getEntityWidth() > entity.getEntityX() && HitPhysicsEngineTest.entity.getEntityX() < entity.getEntityX() + entity.getEntityWidth()) {
                return true;
            }
        }

        return false;
    }

    public static void Jump(){
        if (!isJump && !isInAir()) {
            isJump = true;
            for (int i = 0; i < 10; i++) {
                if (TouchTop(5)) break;
                entity.moveEntity(0, -5);
                try {
                    Thread.sleep(6);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            for (int i = 0; i < 10; i++) {
                if (TouchTop(3)) break;
                entity.moveEntity(0, -3);
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            for (int i = 0; i < 10; i++) {
                if (TouchTop(3)) break;
                entity.moveEntity(0, 3);
                try {
                    Thread.sleep(7);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            isJump = false;
        }
    }
    
    public static boolean isInAir(){
        EntityPane.Edge edge = pane.getEntityHitEdge(entity);
        boolean isHit = false;
        for (Entity entitys : entities) {
            EntityPane.ContactSurface contactSurface = pane.getEntityHitContactSurface(entity, entitys, 10);
            if (contactSurface == EntityPane.ContactSurface.BOTTOM || contactSurface == EntityPane.ContactSurface.BOTTOM_LEFT || contactSurface == EntityPane.ContactSurface.BOTTOM_RIGHT
                    && entity.getEntityX() + entity.getEntityWidth() > entitys.getEntityX() && entity.getEntityX() < entitys.getEntityX() + entitys.getEntityWidth()) {
                isHit = true;
                break;
            }
        }
        return edge != EntityPane.Edge.BOTTOM_RIGHT && edge!= EntityPane.Edge.BOTTOM && edge!= EntityPane.Edge.BOTTOM_LEFT && !isHit;
    }

    static AtomicInteger x = new AtomicInteger(0);
    static AtomicInteger y = new AtomicInteger(0);
    static boolean isPutting = false;

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            while (true) {
                if (isPutting) {
                    RectEntity rect = new RectEntity(
                            "Rect", pane.randomId(), x.get(), y.get(), 30, 30);
                    rect.setFill(true);
                    rect.setEntityColor(color);
                    entities.add(rect);
                    pane.addEntity(rect);
                } else {
                    try {
                        synchronized (x) {
                            x.wait();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    synchronized (y) {
                        y.wait(10);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        color = new Color(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isPutting = true;

                    try {
                        synchronized (x) {
                            x.notifyAll();
                        }
                    } catch (Exception e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isPutting = false;
                }
            }
        });
        pane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                x.set(e.getX());
                y.set(e.getY());
            }
        });

        pane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_S){
                   // System.out.println("Shift");
                    entity.setEntityHeight(25);
                    speed = 1;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_S){
                   // System.out.println("Shift");
                    entity.setEntityHeight(30);
                    speed = 3;
                }
            }
        });

        JFrame frame = new JFrame("HitPhysicsEngineTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(pane);


        BufferedImage image = ImageIO.read(HitPhysicsEngineTest.class.getResource("/test.png"));
        entity = new ImageEntity(image, "Test", 1,
                0, 0, 30, 30);

        new Thread(() -> {
            while (true) {
                pane.repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        entity.addEntityLoopLogic(runDelay -> {
            if (entity.getEntityY() > pane.getHeight() - entity.getEntityHeight()) {
                entity.setEntityY(pane.getHeight() - entity.getEntityHeight());
            }

            if (isInAir() && !isJump) {

                entity.moveEntity(0, 10);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                for (Entity entitys : entities) {
                    EntityPane.ContactSurface contactSurface = pane.getEntityHitContactSurface(entity, entitys, 10);
                    if (contactSurface == EntityPane.ContactSurface.BOTTOM || contactSurface == EntityPane.ContactSurface.BOTTOM_LEFT || contactSurface == EntityPane.ContactSurface.BOTTOM_RIGHT
                            && entity.getEntityX() + entity.getEntityWidth() > entitys.getEntityX() && entity.getEntityX() < entitys.getEntityX() + entitys.getEntityWidth()){
                        if (entity.getEntityY() + entity.getEntityHeight() > entitys.getEntityY()){
                            entity.moveEntityTo(entity.getEntityX(), entitys.getEntityY() - entity.getEntityHeight());
                        }
                    }
                }
            }
        });
        
        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {

            @Override
            public void onKeyDowning(int key) {
                switch (key){
                    case KeyEvent.VK_A -> {
                        EntityPane.Edge edge = pane.getEntityHitEdge(entity);

                        boolean isHit = false;
                        int x = 0;
                        for (Entity entitys : entities) {
                            EntityPane.ContactSurface contactSurface = pane.getEntityHitContactSurface(entitys, entity, speed);
                            if (contactSurface == EntityPane.ContactSurface.LEFT || contactSurface == EntityPane.ContactSurface.BOTTOM_LEFT || contactSurface == EntityPane.ContactSurface.TOP_LEFT
                                && entity.getEntityY() - entity.getEntityHeight() < entitys.getEntityY() && entity.getEntityY() > entitys.getEntityY() + entitys.getEntityHeight()) {
                                x = entitys.getEntityX() + entitys.getEntityWidth();
                                isHit = true;
                                break;
                            }
                        }
                        if(edge != EntityPane.Edge.LEFT && edge != EntityPane.Edge.BOTTOM_LEFT && edge != EntityPane.Edge.TOP_LEFT && !isHit)
                            entity.moveEntity(-speed, 0);
                        if(edge == EntityPane.Edge.LEFT || edge == EntityPane.Edge.BOTTOM_LEFT || edge == EntityPane.Edge.TOP_LEFT)
                            entity.moveEntityTo(0, entity.getEntityY());
                        if (isHit)
                            entity.moveEntityTo(x, entity.getEntityY());
                    }
                    
                    case KeyEvent.VK_D -> {
                        EntityPane.Edge edge = pane.getEntityHitEdge(entity);

                        boolean isHit = false;
                        int x = 0;
                        for (Entity entitys : entities) {
                            EntityPane.ContactSurface contactSurface = pane.getEntityHitContactSurface(entitys, entity, speed);
                            if (contactSurface == EntityPane.ContactSurface.RIGHT || contactSurface == EntityPane.ContactSurface.BOTTOM_RIGHT || contactSurface == EntityPane.ContactSurface.TOP_RIGHT
                                && entity.getEntityY() - entity.getEntityHeight() < entitys.getEntityY() && entity.getEntityY() > entitys.getEntityY() + entitys.getEntityHeight()) {
                                x = entitys.getEntityX();
                                isHit = true;
                                break;
                            }
                        }
                        if(edge != EntityPane.Edge.RIGHT && edge != EntityPane.Edge.BOTTOM_RIGHT && edge != EntityPane.Edge.TOP_RIGHT && !isHit)
                            entity.moveEntity(speed, 0);
                        if(edge == EntityPane.Edge.RIGHT || edge == EntityPane.Edge.BOTTOM_RIGHT || edge == EntityPane.Edge.TOP_RIGHT)
                            entity.moveEntityTo(pane.getWidth() - entity.getEntityWidth(), entity.getEntityY());
                        if (isHit)
                            entity.moveEntityTo(x - entity.getEntityWidth(), entity.getEntityY());
                    }

                    case KeyEvent.VK_SPACE -> new Thread(HitPhysicsEngineTest::Jump).start();
                }
            }
        });

        pane.addEntity(entity);
        frame.setVisible(true);
    }
}
