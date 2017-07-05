package de.diavololoop.game.entitys;

import de.diavololoop.gui.GuiImage;
import org.joml.Vector2d;

/**
 * Created by gast2 on 20.06.17.
 */
public abstract class Entity {

    protected Vector2d position = new Vector2d();
    protected double width = 0.5;
    protected double height = 0.5;

    protected boolean isAlive = true;

    public boolean isAlive(){
        return isAlive;
    }


    public double getX(){
        return position.x;
    }
    public double getY(){
        return position.y;
    }
    public double getWidth(){
        return width;
    }
    public double getHeight(){
        return height;
    }

    public abstract GuiImage getImage();

    public void gameTick(double dt){

    }


}
