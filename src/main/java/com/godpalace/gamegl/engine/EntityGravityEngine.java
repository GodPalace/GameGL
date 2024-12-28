package com.godpalace.gamegl.engine;

import com.godpalace.gamegl.entity.Entity;

public class EntityGravityEngine extends EntityPhysicsEngine {
    public static final double EARTH_GRAVITY = 9.81D;  // 地球的重力加速度
    public static final double MOON_GRAVITY = 1.62D;  // 月球的重力加速度
    public static final double JUPITER_GRAVITY = 24.79D;  // 木星的重力加速度
    public static final double SATURN_GRAVITY = 10.44D;  // 土星的重力加速度
    public static final double URANUS_GRAVITY = 8.87D;  // 天王星的重力加速度
    public static final double NEPTUNE_GRAVITY = 11.15D;  // 海王星的重力加速度
    public static final double PLUTO_GRAVITY = 0.61D;  // 冥王星的重力加速度
    public static final double SUN_GRAVITY = 274.13D;  // 太阳的重力加速度

    public static final double DEFAULT_GRAVITY = EARTH_GRAVITY;  // 默认重力加速度

    private final double gravitationalAcceleration;

    public EntityGravityEngine() {
        this(DEFAULT_GRAVITY);
    }

    public EntityGravityEngine(double gravitationalAcceleration) {
        super();
        this.gravitationalAcceleration = gravitationalAcceleration;
    }

    @Override
    public void update(long time) {
        double m = (gravitationalAcceleration / 2000000.0D) * time * time;
        double p = meterToPixel(m);

        entity.setEntityY((int) (startY + p));
    }
}
