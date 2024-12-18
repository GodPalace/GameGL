package com.godpalace.gamegl.entity.logic;

public interface EntityKeyboardLogic {
    void onKeyDown(int key);
    void onKeyUp(int key);

    enum LogicType {
        DOWN, UP
    }
}
