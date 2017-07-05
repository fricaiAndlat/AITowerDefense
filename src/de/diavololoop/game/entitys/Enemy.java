package de.diavololoop.game.entitys;

import de.diavololoop.game.Resource;
import de.diavololoop.game.map.Map;
import de.diavololoop.game.map.turret.Turret;
import de.diavololoop.game.pathfinding.Edge;
import de.diavololoop.gui.GuiImage;
import org.joml.Vector2d;

import java.util.Iterator;
import java.util.List;

/**
 * Created by gast2 on 20.06.17.
 */
public class Enemy extends Entity {

    public final static double LIFE_MAX = 100;

    private Map map;
    private List<Edge> edges;

    private Iterator<Edge> iter;
    private Edge edge;
    private double progress = 0;

    private double damageMelee = 100; //dps

    private final static double VALUE = 2;

    private double life = LIFE_MAX;

    private boolean isAtTarget = false;

    public Enemy(Map map, Vector2d spawn, Vector2d target){
        this.map = map;
        this.edges = map.getWay(spawn, target);
        this.iter = edges.iterator();
        edge = iter.next();

        super.position.x = spawn.x;
        super.position.y = spawn.y;
    }

    public void findNewPath(Vector2d target){
        synchronized (this) {

            this.edges = map.getWay(edge.target, target);
            this.iter = edges.iterator();
        }

    }

    @Override
    public void gameTick(double dt){

        if(!isAlive){
            return;
        }

        if(life < 0){
            isAlive = false;
            map.addDeath(position.x, position.y, VALUE);
        }


        if(progress > edge.length){

            if(iter.hasNext()){
                progress -= edge.length;

                synchronized(this) {
                    edge = iter.next();
                }

            }else{
                if(!isAtTarget){
                    onTargetArrive();
                    isAtTarget = true;
                }

                return;
            }

        }
        Turret turret = map.intersectTurret(position.x, position.y, width, height);

        if(turret != null){

            turret.addDamage(  dt*damageMelee  );

            return;
        }

        progress += dt * 2;

        double x = progress/edge.length;

        edge.start.lerp(edge.target, x, super.position);

    }

    public double distanceTo(Vector2d other){
        return other.distance(position);
    }

    private void onTargetArrive() {
        isAlive = false;
    }

    @Override
    public GuiImage getImage() {
        return Resource.current().image("monster2");
    }

    public void damage(double damage) {
        life -= damage;
    }

    public Vector2d getPosition() {
        return position;
    }
}
