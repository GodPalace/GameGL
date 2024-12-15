package com.godpalace.gamegl.test;

import com.godpalace.gamegl.entity.Entity;
import com.godpalace.gamegl.entity.EntityPane;
import com.godpalace.gamegl.entity.ImageEntity;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogicAdapter;
import com.godpalace.gamegl.entity.logic.EntityLoopLogic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;

public class Test2 {


    public static EntityPane pane = new EntityPane();
    public static ImageEntity entity;
    public static boolean isJumping = false;
    public static boolean isInAir(Entity entity){
        EntityPane.Edge edge = pane.getEntityHitEdge(entity);
        return edge != EntityPane.Edge.BOTTOM && edge != EntityPane.Edge.BOTTOM_LEFT && edge != EntityPane.Edge.BOTTOM_RIGHT;
    }
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Test2");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image img = ImageIO.read(new FileInputStream("src/test/resources/test.png"));
        entity = new ImageEntity(img, "test", 1, 0, 0, 30, 30);
        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {
            @Override
            public void onKeyTyped(int key) {
                if (key == KeyEvent.VK_A) {
                    entity.moveEntity(-12, 0);
                }
                if (key == KeyEvent.VK_D) {
                    entity.moveEntity(12, 0);
                }
            }
        });
        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {
            @Override
            public void onKeyTyped(int key) {
                if (key == KeyEvent.VK_SPACE) {
                    if (!isInAir(entity)) {
                        isJumping = true;
                        for (int i = 0; i < 25; i++) {
                            entity.moveEntity(0, -5);
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        for (int i = 0; i < 15; i++) {
                            entity.moveEntity(0, -1);
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        isJumping = false;
                    }
                }
            }
        });

        entity.addEntityLoopLogic(runDelay -> {
            if(isInAir(entity) && !isJumping)
                entity.moveEntity(0, 10);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        pane.addEntity(entity);
        frame.add(pane);
        frame.setVisible(true);
    }
}
