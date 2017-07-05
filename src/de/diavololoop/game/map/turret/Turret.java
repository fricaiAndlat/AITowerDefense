package de.diavololoop.game.map.turret;

import de.diavololoop.game.map.Map;
import de.diavololoop.gui.GraphicUserInterface;
import de.diavololoop.gui.GuiImage;
import org.joml.Vector2d;

/**
 * Created by gast2 on 20.06.17.
 */
public class Turret {

    protected GuiImage icon;
    protected double iconSize = 1;

    public double maxHealth = 100;
    public double health = 100;


    protected Vector2d position = new Vector2d();

    protected Map map;

    public Turret(GuiImage icon, Map map){
        this.map = map;
        this.icon = icon;
    }

    public GuiImage getImage(){
        return icon;
    }

    public void onDraw(GraphicUserInterface handle, double zoom){

        handle.drawImage(position.x * zoom, position.y * zoom, iconSize * zoom, iconSize * zoom, icon);

    }

    public void addDamage(double dmg){
        health -= dmg;
        if(health <= 0 && health+dmg > 0){
            map.onTowerDestroyed(this);
        }
    }

    public void setPosition(double x, double y) {
        position.set(x, y);
    }

    public void onTick(double dt) {
    }
}
