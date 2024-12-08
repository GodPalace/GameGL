package com.godpalace.gamegl.entity.logic;

public interface EntityLoopLogic {
    // runDelay: 当前逻辑之前所有逻辑的执行耗时
    void onLoop(long runDelay);
}
