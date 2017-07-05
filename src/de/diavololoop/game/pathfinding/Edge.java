package de.diavololoop.game.pathfinding;

import org.joml.Vector2d;

/**
 * Created by gast2 on 20.06.17.
 */
public class Edge {

    public Vector2d start;
    public Vector2d target;

    public double modifierSpeed;
    public double length;

    public Edge(Vector2d start, Vector2d target, double modifierSpeed){

        this.modifierSpeed = modifierSpeed;
        this.start = start;
        this.target = target;

        length = target.distance(start);
    }


}
