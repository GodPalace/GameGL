package com.godpalace.gamegl.test;

import com.godpalace.gamegl.engine.EntityGravityEngine;
import com.godpalace.gamegl.entity.Entity;
import com.godpalace.gamegl.entity.EntityPane;
import com.godpalace.gamegl.entity.RectEntity;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogicAdapter;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class GravityPhysicsEngineTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Gravity Physics Engine Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        EntityPane pane = new EntityPane();

        Entity entity = new RectEntity("Rect", pane.randomId(), 10, 10, 30, 30);
        EntityGravityEngine engine = new EntityGravityEngine();
        entity.addEntityPhysicsEngine(engine);
        pane.addEntity(entity);
        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyEvent.VK_SPACE) {
                    engine.stop();
                    entity.setEntityY(10);
                    engine.start();
                }
            }

            @Override
            public void onKeyDowning(int key) {
                if (key == KeyEvent.VK_A) {
                    entity.moveEntity(-3, 0);
                } else if (key == KeyEvent.VK_D) {
                    entity.moveEntity(3, 0);
                }
            }
        });

        engine.start();
        frame.setContentPane(pane);
        frame.setVisible(true);
    }
}
