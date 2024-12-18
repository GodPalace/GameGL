package com.godpalace.gamegl.entity.logic;

public interface EntityKeyboardLogic {
    void onKeyDown(int key);
    void onKeyUp(int key);
    void onKeyDowning(int key);

    enum LogicType {
        DOWN, DOWNING, UP
    }
}
