package de.diavololoop.game.effect;

import de.diavololoop.gui.Animation;
import de.diavololoop.gui.GraphicUserInterface;
import org.joml.Vector2d;


/**
 * Created by gast1 on 03.07.17.
 */
public class LineEffect implements Effect {

    private final static Vector2d HORIZONTAL = new Vector2d(1, 0);

    private Animation.AnimationIterator iterator;
    private Vector2d start;
    private Vector2d target;
    private Vector2d temp = new Vector2d();

    private double height;

    private long endTime;

    public boolean isAlive = true;

    public LineEffect(Animation animation, Vector2d start, Vector2d target, double height){
        this.start = start;
        this.target = target;

        this.height = height;

        iterator = animation.getIterator();
        endTime = System.currentTimeMillis() + animation.getDuration();
    }

    @Override
    public void draw(GraphicUserInterface handle, double zoom) {
        if(!isAlive()){
            return;
        }
        target.sub(start, temp);

        double width = temp.length() * zoom;
        double angle = -temp.angle(HORIZONTAL) * 180 / Math.PI;

        temp.mul(0.5);
        temp.add(start);
        temp.mul(zoom);

        handle.drawImage((float)temp.x, (float)temp.y, (float)width, (float)height, angle, iterator.next());
    }

    @Override
    public boolean isAlive() {
        return isAlive && (System.currentTimeMillis() < endTime || iterator.getOrigin().isLoop());
    }
}
