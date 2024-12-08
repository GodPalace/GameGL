package com.godpalace.gamegl.animation;

import javax.swing.*;

public class ResizeAnimation extends Animation {
    private final int width1, height1, width2, height2;
    private final JComponent component;

    public ResizeAnimation(JComponent component,
                           int dwidth, int dheight,
                           int duration, AnimationRefreshRate refreshRate) {
        this(component, component.getWidth(), component.getHeight(),
             component.getWidth() + dwidth, component.getHeight() + dheight,
             duration, refreshRate);
    }

    public ResizeAnimation(JComponent component,
                           int width1, int height1, int width2, int height2,
                           int duration, AnimationRefreshRate refreshRate) {
        super(duration, refreshRate);

        this.component = component;
        this.width1 = width1;
        this.height1 = height1;
        this.width2 = width2;
        this.height2 = height2;
    }

    @Override
    public void update(float progress) {
        component.setSize((int) (width1 + (width2 - width1) * progress),
                          (int) (height1 + (height2 - height1) * progress));
    }
}
