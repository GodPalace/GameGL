package com.godpalace.gamegl.animation;

public enum AnimationRefreshRate {
    ULTRA_HIGH(5.0F), HIGH(10.0F), MEDIUM(20.0F), LOW(50.0F), ULTRA_LOW(100.0F);

    private final float refreshRate;

    AnimationRefreshRate(float refreshRate) {
        this.refreshRate = refreshRate;
    }

    public float getRefreshRate() {
        return refreshRate;
    }
}
