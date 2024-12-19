package com.godpalace.gamegl.test;

import com.godpalace.gamegl.entity.Entity;
import com.godpalace.gamegl.entity.EntityPane;
import com.godpalace.gamegl.entity.EntityUtils;
import com.godpalace.gamegl.entity.ImageEntity;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogicAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Test2 {
    public static EntityPane pane = new EntityPane();
    public static Entity entity;

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Test2");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Image img = ImageIO.read(Test2.class.getResource("/test.png"));
        entity = new ImageEntity(img, "test", pane.randomId(), 0, 0, 30, 30);
        EntityUtils.initMoveLogic(entity, 3);
        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {
            @Override
            public void onKeyDown(int key) {
                if (key == KeyEvent.VK_SPACE) {
                    entity.hide();
                }
            }

            @Override
            public void onKeyUp(int key) {
                if (key == KeyEvent.VK_SPACE) {
                    entity.show();
                }
            }
        });

        pane.addEntity(entity);
        frame.setContentPane(pane);
        frame.setVisible(true);
    }
}
