package com.godpalace.gamegl.test;

import com.godpalace.gamegl.entity.EntityPane;
import com.godpalace.gamegl.entity.EntityUtils;
import com.godpalace.gamegl.entity.ImageEntity;
import com.godpalace.gamegl.entity.attribute.EntityHealthAttribute;
import com.godpalace.gamegl.entity.attribute.EntityIdAttribute;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
        entity = new ImageEntity(img, "test", pane.randomId(), 0, 0, 30, 30);
        entity.setEntityAttribute("Health", health);
        entity.setEntityAttribute("Id", new EntityIdAttribute(entity.getId()));
        EntityUtils.initMoveLogic(entity, 3);

        pane.addEntity(entity);
        frame.setContentPane(pane);
        frame.setVisible(true);
    }
}
