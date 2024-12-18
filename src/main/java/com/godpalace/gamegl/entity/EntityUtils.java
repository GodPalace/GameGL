package com.godpalace.gamegl.entity;

import com.godpalace.gamegl.entity.logic.EntityKeyboardLogicAdapter;

import java.awt.event.KeyEvent;

public class EntityUtils {
    public static void initMoveLogic(Entity entity, int speed) {
        entity.addEntityKeyboardLogic(new EntityKeyboardLogicAdapter() {
            @Override
            public void onKeyDowning(int key) {
                switch (key) {
                    case KeyEvent.VK_A -> entity.moveEntity(speed * -1, 0);
                    case KeyEvent.VK_D -> entity.moveEntity(speed, 0);
                    case KeyEvent.VK_W -> entity.moveEntity(0, speed * -1);
                    case KeyEvent.VK_S -> entity.moveEntity(0, speed);
                }
            }
        });
    }
}
