package com.godpalace.gamegl.entity;


import java.awt.*;
import java.util.ArrayList;

public class MapEntityPane extends EntityPane {
    private Entity MainEntity;
    private int TureOriginPointX, TureOriginPointY;
    private int originPointX = 0, originPointY = 0;
    private int borderX1, borderY1, borderX2, borderY2;
    private boolean borderEnabled = false;
    private int range;
    private final ArrayList<Entity> notMoveEntities = new ArrayList<>();
    private originLocationType originLoc;
    private final Thread originThread = new Thread(()->{
        while(true){
            if(originLoc == originLocationType.MIDDLE){
                TureOriginPointX = this.getWidth()/2;
                TureOriginPointY = this.getHeight()/2;
            }
            if(originLoc == originLocationType.BOTTOM_MIDDLE){
                TureOriginPointX = this.getWidth()/2;
                TureOriginPointY = this.getHeight();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });
    private final Thread EntityThread = new Thread(()->{
        while(true){
            if(MainEntity == null) continue;
            int x = MainEntity.getEntityX() - TureOriginPointX;
            int y = MainEntity.getEntityY() - TureOriginPointY;
            if(Math.abs(x) > range && x < 0){
                if (borderEnabled) {
                    int borderX = borderX1 - originPointX + TureOriginPointX;
                    if (borderX < 0) {
                        int dx = Math.abs(x) - range;
                        MainEntity.setEntityX(TureOriginPointX - range);
                        moveMap(dx, 0);
                    } else {
                        Edge edge = this.getEntityHitEdge(MainEntity);
                        if (edge == Edge.LEFT || edge == Edge.TOP_LEFT || edge == Edge.BOTTOM_LEFT)
                            MainEntity.setEntityX(0);
                    }
                } else {
                    int dx = Math.abs(x) - range;
                    MainEntity.setEntityX(TureOriginPointX - range);
                    moveMap(dx, 0);
                }
            }
            if(Math.abs(x) > range && x > 0){
                if (borderEnabled) {
                    int borderX = borderX2 - originPointX + TureOriginPointX;
                    if (borderX > this.getWidth()) {
                        int dx = Math.abs(x) - range;
                        MainEntity.setEntityX(TureOriginPointX + range);
                        moveMap(-dx, 0);
                    } else {
                        Edge edge = this.getEntityHitEdge(MainEntity);
                        if (edge == Edge.RIGHT || edge == Edge.TOP_RIGHT || edge == Edge.BOTTOM_RIGHT)
                            MainEntity.setEntityX(this.getWidth() - MainEntity.getEntityWidth());
                    }
                } else {
                    int dx = Math.abs(x) - range;
                    MainEntity.setEntityX(TureOriginPointX + range);
                    moveMap(-dx, 0);
                }
            }
            if(Math.abs(y) > range && y < 0){
                if (borderEnabled) {
                    int borderY = borderY1 - originPointY + TureOriginPointY;
                    if (borderY < 0) {
                        int dy = Math.abs(y) - range;
                        MainEntity.setEntityY(TureOriginPointY - range);
                        moveMap(0, dy);
                    } else {
                        Edge edge = this.getEntityHitEdge(MainEntity);
                        if (edge == Edge.TOP || edge == Edge.TOP_LEFT || edge == Edge.TOP_RIGHT)
                            MainEntity.setEntityY(0);
                    }
                } else {
                    int dy = Math.abs(y) - range;
                    MainEntity.setEntityY(TureOriginPointY - range);
                    moveMap(0, dy);
                }
            }
            if(Math.abs(y) > range && y > 0){
                if (borderEnabled) {
                    int borderY = borderY2 - originPointY + TureOriginPointY;
                    if (borderY > this.getHeight()) {
                        int dy = Math.abs(y) - range;
                        MainEntity.setEntityY(TureOriginPointY + range);
                        moveMap(0, -dy);
                    } else {
                        Edge edge = this.getEntityHitEdge(MainEntity);
                        if (edge == Edge.BOTTOM || edge == Edge.BOTTOM_LEFT || edge == Edge.BOTTOM_RIGHT)
                            MainEntity.setEntityY(this.getHeight() - MainEntity.getEntityHeight());
                    }
                } else {
                    int dy = Math.abs(y) - range;
                    MainEntity.setEntityY(TureOriginPointY + range);
                    moveMap(0, -dy);
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });
    public enum originLocationType {
        MIDDLE, BOTTOM_MIDDLE, NONE
    }

    public MapEntityPane(){
        super();
    }

    public void init(Entity MainEntity, int range, originLocationType loc){
        this.MainEntity = MainEntity;
        this.range = range;
        this.originLoc = loc;
        originThread.start();
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.MainEntity.setEntityX(TureOriginPointX);
        this.MainEntity.setEntityY(TureOriginPointY);
        EntityThread.start();
    }

    public void moveMap(int dx, int dy) {
        originPointX -= dx;
        originPointY -= dy;
        this.forEach(entity -> {
            if(entity.getId() != MainEntity.getId() && !notMoveEntities.contains(entity)) {
                entity.moveEntity(dx, dy);
            }
        });
        repaint();
    }

    @Override
    protected void printComponent(Graphics g) {
        super.printComponent(g);
    }

    public void addNotMoveEntity(Entity entity){
        notMoveEntities.add(entity);
    }
    public void removeNotMoveEntity(Entity entity){
       notMoveEntities.remove(entity);
    }
    public ArrayList<Entity> getNotMoveEntities(){
        return notMoveEntities;
    }
    public void setMainEntity(Entity entity){
        MainEntity = entity;
    }
    public Entity getMainEntity(){
        return MainEntity;
    }
    public void setRange(int range){
        this.range = range;
    }
    public int getRange(){
        return range;
    }
    public void setOriginLocationType(originLocationType loc){
        originLoc = loc;
    }
    public originLocationType getOriginLocationType(){
        return originLoc;
    }
    public int getOriginPointX(){
        return originPointX;
    }
    public int getOriginPointY(){
        return originPointY;
    }
    public int getTrueOriginPointX(){
        return TureOriginPointX;
    }
    public int getTrueOriginPointY() {
        return TureOriginPointY;
    }
    public void setOriginLocation(int x, int y){
        TureOriginPointX = x;
        TureOriginPointY = y;
        if (EntityThread.isAlive()){
            EntityThread.interrupt();
        }
    }
    public void setBorder(int x1, int y1, int x2, int y2){
        borderX1 = x1;
        borderY1 = y1;
        borderX2 = x2;
        borderY2 = y2;
        borderEnabled = true;
    }
    public void setBorderEnabled(boolean enabled){
        borderEnabled = enabled;
    }
    public void  setEntityX(Entity entity, int x){
        int dx = x - originPointX;
        entity.setEntityX(dx + TureOriginPointX);
    }
    public void  setEntityY(Entity entity, int y){
        int dy = y - originPointY;
        entity.setEntityY(dy + TureOriginPointY);
    }
    public int getEntityX(Entity entity){
        int dx = entity.getEntityX() - TureOriginPointX;
        return originPointX + dx;
    }
    public int getEntityY(Entity entity){
        int dy = entity.getEntityY() - TureOriginPointY;
        return originPointY + dy;
    }
}
