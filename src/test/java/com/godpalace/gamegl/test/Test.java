package com.godpalace.gamegl.test;

import com.godpalace.gamegl.entity.Entity;
import com.godpalace.gamegl.entity.EntityPane;
import com.godpalace.gamegl.entity.RectEntity;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogicAdapter;
import com.godpalace.gamegl.entity.logic.EntityLoopLogic;
import com.godpalace.gamegl.setter.Data;
import com.godpalace.gamegl.setter.Database;
import com.godpalace.gamegl.setter.FileDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;


public class Test {

    static RectEntity entity, entity2;
    public static void main(String[] args) throws Exception {


        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(500, 500, 800, 600);

        EntityPane entityPane = new EntityPane();
        new Thread(() -> {
            while (true) {
                entityPane.repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        entity = new RectEntity("test", 0, 0, 0, 50, 50);
        entity.setEntityColor(Color.CYAN);
        entity.setFill(true);
        entity2 = new RectEntity("test2", 1, 100, 100, 50, 50);
        entity2.setEntityColor(Color.GREEN);
        entity2.setFill(true);

        entity.setEntityNameColor(Color.BLACK);
        entity.setShowName(true);
        entity.setEntityLoopLogic(runDelay -> {
            EntityPane.ContactSurface contactSurface = entityPane.EntityHitContactSurface(entity,entity2,20);
            switch (contactSurface) {
                case TOP -> entity.setEntityName("Top");
                case BOTTOM -> entity.setEntityName("Bottom");
                case LEFT -> entity.setEntityName("Left");
                case RIGHT -> entity.setEntityName("Right");
                case MIDDLE -> entity.setEntityName("Middle");
                case TOP_LEFT -> entity.setEntityName("Top Left");
                case TOP_RIGHT -> entity.setEntityName("Top Right");
                case BOTTOM_LEFT -> entity.setEntityName("Bottom Left");
                case BOTTOM_RIGHT -> entity.setEntityName("Bottom Right");
                case NONE -> entity.setEntityName("None");
            }
        });

        entity2.setEntityNameColor(Color.BLACK);
        entity2.setShowName(true);
        entity2.setEntityLoopLogic(runDelay -> {
            EntityPane.ContactSurface contactSurface = entityPane.EntityHitContactSurface(entity2,entity,20);
            switch (contactSurface) {
                case TOP -> entity2.setEntityName("Top");
                case BOTTOM -> entity2.setEntityName("Bottom");
                case LEFT -> entity2.setEntityName("Left");
                case RIGHT -> entity2.setEntityName("Right");
                case MIDDLE -> entity2.setEntityName("Middle");
                case TOP_LEFT -> entity2.setEntityName("Top Left");
                case TOP_RIGHT -> entity2.setEntityName("Top Right");
                case BOTTOM_LEFT -> entity2.setEntityName("Bottom Left");
                case BOTTOM_RIGHT -> entity2.setEntityName("Bottom Right");
                case NONE -> entity2.setEntityName("None");
            }
        });
        entity.setEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyEvent.VK_W) {
                    entity.moveEntity(0, -20);
                }
                if (key == KeyEvent.VK_S) {
                    entity.moveEntity(0, 20);
                }
                if (key == KeyEvent.VK_A) {
                    entity.moveEntity(-20, 0);
                }
                if (key == KeyEvent.VK_D) {
                    entity.moveEntity(20, 0);
                }
                if (key == KeyEvent.VK_UP) {
                    entity2.moveEntity(0, -20);
                }
                if (key == KeyEvent.VK_DOWN) {
                    entity2.moveEntity(0, 20);
                }
                if (key == KeyEvent.VK_LEFT) {
                    entity2.moveEntity(-20, 0);
                }
                if (key == KeyEvent.VK_RIGHT) {
                    entity2.moveEntity(20, 0);
                }
            }
        });

        entityPane.addEntity(entity);
        entityPane.addEntity(entity2);
        frame.add(entityPane);
        frame.setVisible(true);

    }
}
