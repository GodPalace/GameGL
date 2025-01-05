package com.godpalace.gamegl.entity;


import java.awt.*;
//////
public class MapEntityPane extends EntityPane {
    private Entity MainEntity;
    private int TureOriginPointX, TureOriginPointY;
    private int originPointX = 0, originPointY = 0;
    private int range;
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
            //System.out.println(TureOriginPointX + " " + TureOriginPointY + "\n" + MainEntity.getEntityX() + " " + MainEntity.getEntityY());
            int x = MainEntity.getEntityX() - TureOriginPointX;
            int y = MainEntity.getEntityY() - TureOriginPointY;
            if(Math.abs(x) > range && x < 0){
                int dx = Math.abs(x) - range;
                MainEntity.setEntityX(TureOriginPointX - range);
                moveMap(dx, 0);
            }
            if(Math.abs(x) > range && x > 0){
                int dx = Math.abs(x) - range;
                MainEntity.setEntityX(TureOriginPointX + range);
                moveMap(-dx, 0);
            }
            if(Math.abs(y) > range && y < 0){
                int dy = Math.abs(y) - range;
                MainEntity.setEntityY(TureOriginPointY - range);
                moveMap(0, dy);
            }
            if(Math.abs(y) > range && y > 0){
                int dy = Math.abs(y) - range;
                MainEntity.setEntityY(TureOriginPointY + range);
                moveMap(0, -dy);
            }
            try {//
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

    public void moveMap(int dx, int dy) {
        originPointX -= dx;
        originPointY -= dy;
        this.forEach(entity -> entity.moveEntity(dx, dy));
        repaint();
    }

    @Override
    protected void printComponent(Graphics g) {
        super.printComponent(g);
    }

    public void setMainEntity(Entity entity){
        MainEntity = entity;
        System.out.println(TureOriginPointX + " " + TureOriginPointY);
        MainEntity.setEntityX(TureOriginPointX);
        MainEntity.setEntityY(TureOriginPointY);
        if(EntityThread.isAlive()){
            EntityThread.interrupt();
        }
        EntityThread.start();
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
        if(originThread.isAlive()){
            originThread.interrupt();
        }
        originThread.start();
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
        if(originThread.isAlive()){
            originThread.interrupt();
        }
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
