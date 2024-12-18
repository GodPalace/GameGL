package com.godpalace.gamegl.test;

import com.godpalace.gamegl.entity.EntityPane;
import com.godpalace.gamegl.entity.ImageEntity;
import com.godpalace.gamegl.entity.attribute.EntityHealthAttribute;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogicAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Test2 {
    public static EntityPane pane = new EntityPane();
    public static ImageEntity entity;
    public static EntityHealthAttribute health = new EntityHealthAttribute(100, 50);

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Test2");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Image img = ImageIO.read(Test2.class.getResourceAsStream("/test.png"));
        entity = new ImageEntity(img, "test", 1, 0, 0, 30, 30);
        entity.setEntityAttribute("Health", health);

        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {
            @Override
            public void onKeyDown(int key) {
                switch (key) {
                    case KeyEvent.VK_A -> entity.moveEntity(-3, 0);
                    case KeyEvent.VK_D -> entity.moveEntity(3, 0);
                    case KeyEvent.VK_W -> entity.moveEntity(0, -3);
                    case KeyEvent.VK_S -> entity.moveEntity(0, 3);
                }
            }
        });

        pane.addEntity(entity);
        frame.setContentPane(pane);
        frame.setVisible(true);
    }
}
