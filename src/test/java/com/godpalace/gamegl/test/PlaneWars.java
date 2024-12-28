package com.godpalace.gamegl.test;

import com.godpalace.gamegl.entity.*;
import com.godpalace.gamegl.entity.attribute.EntityAttribute;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogicAdapter;
import com.godpalace.gamegl.util.DialogUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class PlaneWars {
    public static Font font = new Font("Arial", Font.PLAIN, 16);

    public static final Random rand = new Random();
    public static EntityPane pane = new EntityPane();
    public static Entity entity;
    public static EntityAttribute<Integer> score = new EntityAttribute<>(0) {
        @Override
        public void update(Graphics g, Entity entity) {
            g.setFont(font);
            g.drawString("Score: " + getValue(), 10, 20);
        }
    };

    public static boolean isSuper = false;
    public static AtomicInteger time = new AtomicInteger(8);

    public static final LinkedList<Entity> entities = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Plane wars");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(pane);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                entity.moveEntityTo(entity.getEntityX(), frame.getHeight() - 67);

                int dx = Math.max(entity.getEntityX(), 0);
                entity.moveEntityTo(dx, entity.getEntityY());

                dx = Math.min(entity.getEntityX(), pane.getWidth() - entity.getEntityWidth());
                entity.moveEntityTo(dx, entity.getEntityY());
            }
        });

        BufferedImage img = ImageIO.read(PlaneWars.class.getResource("/test.png"));
        entity = new ImageEntity(img, "Plane", pane.randomId(), 0, 0, 30, 30);
        entity.setEntityAttribute("Score", score);

        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {
            @Override
            public void onKeyDowning(int key) {
                switch (key) {
                    case KeyEvent.VK_A -> {
                        int dx = Math.max(entity.getEntityX() - 5, 0);
                        entity.moveEntityTo(dx, entity.getEntityY());
                    }

                    case KeyEvent.VK_D -> {
                        int dx = Math.min(entity.getEntityX() + 5,
                                pane.getWidth() - entity.getEntityWidth());
                        entity.moveEntityTo(dx, entity.getEntityY());
                    }
                }
            }

            @Override
            public void onKeyDown(int key) {
                if (key == KeyEvent.VK_SPACE) {
                    RectEntity e = new RectEntity("Cannonball", pane.randomId(),
                            entity.getEntityX() + entity.getEntityWidth() / 2 - 4,
                            entity.getEntityY(), (isSuper ? 6 : 2), 10);

                    e.setFill(isSuper);
                    e.setEntityColor(Color.RED);
                    e.addEntityLoopLogic(runDelay -> {
                        e.moveEntity(0, -10 * (isSuper ? 2 : 1));

                        if (e.getEntityY() < -10) {
                            pane.removeEntity(e);
                            entities.remove(e);
                        }
                    });

                    entities.add(e);
                    pane.addEntity(e, 1);
                }
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int x = rand.nextInt(frame.getWidth() - entity.getEntityWidth());

                RectEntity e = new RectEntity("Enemy", pane.randomId(),
                        x, -64, 32, 32);

                e.addEntityLoopLogic(runDelay -> {
                    e.moveEntity(0, 2);

                    for (Entity e2 : entities) {
                        if (pane.isEntityHit(e, e2)) {
                            pane.removeEntity(e);
                            pane.removeEntity(e2);
                            entities.remove(e2);

                            score.setValue(score.getValue() + 1);
                            return;
                        }
                    }

                    if (e.getEntityY() > frame.getHeight()) {
                        pane.removeAllEntities();
                        timer.cancel();
                        frame.dispose();

                        DialogUtil.showMessage(
                                "Game Over, Score: " + score.getValue(),
                                Color.BLACK, Color.WHITE,
                                font,
                                3500);

                        System.exit(0);
                    }
                });

                pane.addEntity(e);
            }
        }, 3000, 800);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    while (time.get() > 0) {
                        Thread.sleep(1000);
                        time.set(time.get() - 1);
                    }

                    isSuper = false;
                    time.set(8);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int x = rand.nextInt(frame.getWidth() - entity.getEntityWidth());

                RectEntity e = new RectEntity("Super", pane.randomId(),
                        x, -128, 64, 64);
                e.setEntityColor(Color.GREEN);
                e.setFill(true);

                e.addEntityLoopLogic(runDelay -> {
                    e.moveEntity(0, 1);

                    if (pane.isEntityHit(e, entity)) {
                        pane.removeEntity(e);

                        if (isSuper) {
                            time.set(8);
                        } else {
                            timer.schedule(task, 0);
                        }

                        isSuper = true;
                        return;
                    }

                    if (e.getEntityY() > frame.getHeight()) {
                        pane.removeEntity(e);
                    }
                });

                pane.addEntity(e);
            }
        }, 10000, 2000);

        pane.setBackground(Color.LIGHT_GRAY);
        pane.addEntity(entity, 2);
        frame.setVisible(true);
    }
}
