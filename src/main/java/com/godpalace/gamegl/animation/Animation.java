package com.godpalace.gamegl.animation;

import java.util.concurrent.atomic.AtomicReference;

public abstract class Animation {
    private final Thread thread;
    private final int duration;
    private final AnimationRefreshRate refreshRate;
    private final AtomicReference<Float> progress;

    public Animation(int duration, AnimationRefreshRate refreshRate) {
        this.thread = new Thread(this::runner);
        this.duration = duration;
        this.refreshRate = refreshRate;
        this.progress = new AtomicReference<>(0.0F);
    }

    public void play() {
        thread.start();
    }

    public void setProgress(float progress) {
        this.progress.set(progress);
    }

    private void runner() {
        while (progress.get() <= 1) {
            update(progress.get());
            progress.set(progress.get() + refreshRate.getRefreshRate() / duration);

            try {
                synchronized (this) {
                    wait((long) refreshRate.getRefreshRate());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public abstract void update(float progress);
}
