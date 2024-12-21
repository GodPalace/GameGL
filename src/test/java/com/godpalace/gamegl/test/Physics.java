package com.godpalace.gamegl.test;

import com.godpalace.gamegl.entity.Entity;
import com.godpalace.gamegl.entity.EntityPane;
import com.godpalace.gamegl.entity.ImageEntity;
import com.godpalace.gamegl.entity.RectEntity;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogic;
import com.godpalace.gamegl.entity.logic.EntityKeyboardLogicAdapter;
import com.godpalace.gamegl.entity.logic.EntityMouseLogic;
import com.godpalace.gamegl.entity.logic.EntityMouseLogicAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class Physics {
    public static EntityPane pane = new EntityPane();
    public static ImageEntity entity;
    public static final LinkedList<Entity> entities = new LinkedList<>();
    public static boolean isJump = false;
    
    public static boolean isInAir(){
        EntityPane.Edge edge = pane.getEntityHitEdge(entity);
        return edge != EntityPane.Edge.BOTTOM_RIGHT && edge!= EntityPane.Edge.BOTTOM && edge!= EntityPane.Edge.BOTTOM_LEFT;
    }
    
    public static void main(String[] args) {
        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1 ){

                    boolean isHave = false;
                    RectEntity rect = new RectEntity("Rect", pane.randomId(), e.getX(), e.getY(), 30, 30);//
                    rect.setFill(true);
                    rect.setEntityColor(Color.CYAN);
                    if (!isHave){
                        entities.add(rect);
                        pane.addEntity(rect);
                    }
                }
            }
        });

        JFrame frame = new JFrame("Physics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocation(400, 400);
        frame.setContentPane(pane);

        entity = new ImageEntity(Physics.class.getResource("/test.png"), "Test", 1,
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
            if (isInAir() && !isJump) {
                entity.moveEntity(0, 11);


                if (entity.getEntityY() > pane.getHeight() - entity.getEntityHeight()) {
                    entity.setEntityY(pane.getHeight() - entity.getEntityHeight());
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        
        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {

            @Override
            public void onKeyDowning(int key) {
                switch (key){
                    case KeyEvent.VK_A -> {
                        EntityPane.Edge edge = pane.getEntityHitEdge(entity);
                        if(edge != EntityPane.Edge.LEFT && edge != EntityPane.Edge.BOTTOM_LEFT && edge != EntityPane.Edge.TOP_LEFT) 
                            entity.moveEntity(-4, 0);
                        if(edge == EntityPane.Edge.LEFT || edge == EntityPane.Edge.BOTTOM_LEFT || edge == EntityPane.Edge.TOP_LEFT)
                            entity.moveEntityTo(0, entity.getEntityY());
                    }
                    
                    case KeyEvent.VK_D -> {
                        EntityPane.Edge edge = pane.getEntityHitEdge(entity);
                        if(edge != EntityPane.Edge.RIGHT && edge != EntityPane.Edge.BOTTOM_RIGHT && edge != EntityPane.Edge.TOP_RIGHT)
                            entity.moveEntity(4, 0);
                        if(edge == EntityPane.Edge.RIGHT || edge == EntityPane.Edge.BOTTOM_RIGHT || edge == EntityPane.Edge.TOP_RIGHT)
                            entity.moveEntityTo(pane.getWidth(), entity.getEntityY());
                    }
                }
            }
        });

        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {
            @Override
            public void onKeyDowning(int key) {
                if (key == KeyEvent.VK_SPACE) {
                    if (!isJump && !isInAir()) {
                        isJump = true;
                        for (int i = 0; i < 25; i++) {
                            entity.moveEntity(0, -5);
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        for (int i = 0; i < 30; i++) {
                            entity.moveEntity(0, 1);
                            try {
                                Thread.sleep(4);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        isJump = false;
                    }
                }
            }
        });
        pane.addEntity(entity);
        frame.setVisible(true);
    }
}
