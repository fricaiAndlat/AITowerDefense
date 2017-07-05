package de.diavololoop.game.effect;

import de.diavololoop.gui.Animation;
import de.diavololoop.gui.GraphicUserInterface;
import org.joml.Vector2d;

/**
 * Created by gast2 on 21.06.17.
 */
public class PointEffect implements Effect{

    Animation.AnimationIterator iterator;

    private Vector2d pos;
    private double angle;
    private double width;
    private double height;

    private long startTime;
    private long endTime;

    public PointEffect(Animation animation, Vector2d pos, double angle, double width, double height){
        iterator = animation.getIterator();

        this.pos = pos;
        this.angle = angle;
        this.width = width;
        this.height = height;


        startTime = System.currentTimeMillis();
        endTime = startTime + animation.getDuration();
    }

    public void draw(GraphicUserInterface handle, double zoom){
        if(!isAlive()){
            return;
        }

        handle.drawImage((float)pos.x, (float)pos.y, (float)width, (float)height, angle, iterator.next());
    }

    public boolean isAlive(){
        return System.currentTimeMillis() < endTime;
    }

}
