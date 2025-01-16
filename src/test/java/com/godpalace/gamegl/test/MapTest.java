package com.godpalace.gamegl.test;

import com.godpalace.gamegl.entity.*;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MapTest {
    public static MapEntityPane pane = new MapEntityPane();
    public static ImageEntity entity;
    public static Entity loc = new Entity("Location", 666, 0, 0, 1, 1) {
        @Override
        public void update(Graphics g) {
            int x = MapTest.pane.getEntityX(entity);
            int y = MapTest.pane.getEntityY(entity);
            g.setFont(new Font("Arial", Font.PLAIN, 25));
            g.drawString("X:"+x+" Y:"+y, 20, 20);
        }
    };
    public static final CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();
    public static Color color;
    public static int speed = 3;

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
                    System.out.println("\rEntity Number: " + entities.size());
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


        JFrame frame = new JFrame("MapTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(pane);


        BufferedImage image = ImageIO.read(HitPhysicsEngineTest.class.getResource("/test.png"));
        entity = new ImageEntity(image, "Test", 1,
                0, 0, 30, 30);
        loc.setEntityColor(Color.BLACK);
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

        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {

            @Override
            public void onKeyDowning(int key) {
                switch (key){
                    case KeyEvent.VK_A -> entity.moveEntity(-speed, 0);

                    case KeyEvent.VK_D -> entity.moveEntity(speed, 0);

                    case KeyEvent.VK_W -> entity.moveEntity(0, -speed);

                    case KeyEvent.VK_S -> entity.moveEntity(0, speed);
                }
            }

            @Override
            public void onKeyDown(int key) {
                switch (key){
                    case KeyEvent.VK_UP -> {
                        speed++;
                        System.out.println("Speed: " + speed);
                    }

                    case KeyEvent.VK_DOWN -> {
                        speed--;
                        System.out.println("Speed: " + speed);
                    }

                    case KeyEvent.VK_O -> {
                        entity.setHitBoxEnabled(!entity.isHitBoxEnabled());
                        System.out.println("HitBox: " + entity.isHitBoxEnabled());
                    }
                }
            }
        });

        frame.setVisible(true);
        BufferedImage image2 = ImageIO.read(HitPhysicsEngineTest.class.getResource("/666.png"));
        pane.setBackground(image2);
        pane.setBackgroundMove(false);
        pane.addEntity(entity);
        pane.addEntity(loc, 2);
        pane.addNotMoveEntity(loc);
        pane.setBorder(-500, -500, 500, 500);
        pane.init(entity, 30);
        pane.startEntityHitDetect();
    }
}
