package com.godpalace.gamegl.engine;

import com.godpalace.gamegl.entity.Entity;

import java.awt.*;
import java.util.Arrays;

public abstract class EntityPhysicsEngine {
    private static final int DPI = Toolkit.getDefaultToolkit().getScreenResolution();

    private final Object lock = new Object();

    protected boolean isStarted;
    protected long startTime = -1;

    protected Entity entity;
    protected int startX, startY;

    public void start() {
        isStarted = true;
        startTime = System.currentTimeMillis();

        startX = entity.getEntityX();
        startY = entity.getEntityY();

        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public long getStartTime() {
        return startTime;
    }

    public void stop() {
        isStarted = false;
        startTime = -1;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void run() {
        while (true) {
            try {
                if (!isStarted) {
                    synchronized (lock) {
                        lock.wait();
                    }
                }

                long time = System.currentTimeMillis() - startTime;
                update(time);

                synchronized (this) {
                    wait(10);
                }
            } catch (Exception e) {
                System.err.println(Arrays.toString(e.getStackTrace()));
                break;
            }
        }
    }

    protected static double meterToPixel(double meters) {
        return meters * DPI;
    }

    public abstract void update(long time);
}
