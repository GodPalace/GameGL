package com.godpalace.gamegl.entity.logic;

public interface EntityMouseLogic {
    void onMouseDown(int button, int count, int x, int y);
    void onMouseUp(int button, int count, int x, int y);
    void onMouseClick(int button, int count, int x, int y);

    enum LogicType {
        DOWN, UP, CLICK
    }
}
