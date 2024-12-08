package com.godpalace.gamegl.animation;

import javax.swing.*;

public class MoveAnimation extends Animation {
    private final int x1, y1, x2, y2;
    private final JComponent component;

    public MoveAnimation(JComponent component,
                         int dx, int dy,
                         int duration, AnimationRefreshRate refreshRate) {
        this(component, component.getX(), component.getY(),
                component.getX() + dx, component.getY() + dy,
                duration, refreshRate);
    }

    public MoveAnimation(JComponent component,
                         int x1, int y1, int x2, int y2,
                         int duration, AnimationRefreshRate refreshRate) {
        super(duration, refreshRate);

        this.component = component;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void update(float progress) {
        component.setLocation((int) (x1 + (x2 - x1) * progress), (int) (y1 + (y2 - y1) * progress));
        component.repaint();
    }
}
