package de.diavololoop.game.map;

import de.diavololoop.game.Resource;
import de.diavololoop.game.map.turret.Turret;
import de.diavololoop.gui.GraphicUserInterface;
import de.diavololoop.gui.GuiImage;

/**
 * Created by gast2 on 20.06.17.
 */
public class Field {

    private GuiImage emptyImage;

    private Turret turret;

    private int x;
    private int y;
    private int nx;
    private int ny;

    private double deathPunish = 0;

    public Field(int x, int y, int nx, int ny){
        this.x = x;
        this.y = y;
        this.nx = nx;
        this.ny = ny;
        emptyImage = Resource.current().image("tile");
    }

    public GuiImage getImage(){
        if(turret == null){
            return emptyImage;
        }
        if(turret.health <= 0){
            return Resource.current().image("trash");
        }
        return turret.getImage();
    }

    public boolean isBlocked(){
        if(getSpeedMultiplier() > 2){
            //return true;
        }
        return false;
    }

    public double getSpeedMultiplier(){
        if(turret == null){
            return 1 + deathPunish;
        }
        if(turret.health <= 0){
            return 10 + deathPunish;
        }
        return 50 + deathPunish;
    }

    public void setTurret(Turret turret){
        this.turret = turret;
        turret.setPosition(x - nx/2 + 0.5, y - ny/2 + 0.5);
    }

    public void onTick(double dt){
        if(turret != null){
            turret.onTick(dt);
        }
    }


    public Turret getTurret() {
        if(turret == null){
            return null;
        }
        if(turret.health <= 0){
            return null;
        }
        return turret;
    }

    public void addDeath(double value) {
        deathPunish += value;
        //System.out.printf("new speed modifier is %f\r\n", getSpeedMultiplier());
    }

    public void onDraw(GraphicUserInterface<?> handle, double zoom) {

        if(turret!= null && turret.health < 0){
            handle.drawImage((x - nx/2 + 0.5) * zoom, (y - ny/2 + 0.5) * zoom, zoom, zoom, Resource.current().image("trash"));
        }else{
            handle.drawImage((x - nx/2 + 0.5) * zoom, (y - ny/2 + 0.5) * zoom, zoom, zoom, emptyImage);
        }

        if(turret != null && turret.health >= 0){
            turret.onDraw(handle, zoom);
        }

    }
}
