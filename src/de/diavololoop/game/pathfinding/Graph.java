package de.diavololoop.game.pathfinding;

import org.joml.Vector2d;

import java.util.*;

/**
 * Created by gast2 on 20.06.17.
 */
public abstract class Graph {

    public abstract List<Edge> getEdges();

    public List<Edge> getWay(Vector2d start, Vector2d target){

        LinkedList<Vector2d> points = new LinkedList<Vector2d>();
        points.add(start);

        HashSet<Vector2d> finishedNodes = new HashSet<Vector2d>();

        HashMap<Vector2d, Edge> wayTo = new HashMap<>();

        HashMap<Vector2d, Double> distanceTo = new HashMap<>();
        distanceTo.put(start, 0.0);



        while(!points.isEmpty()){
            Vector2d node = points.poll();

            if(finishedNodes.contains(node)){
                continue;
            }

            finishedNodes.add(node);

            getEdges().stream().filter(edge -> edge.start == node).forEach(edge -> {

                //for each edge with start == node

                points.add(edge.target);

                Edge currentEdgeToTarget = wayTo.get(edge.target);

                double newlyDistance = distanceTo.get( node ) + edge.length * edge.modifierSpeed;

                if(currentEdgeToTarget == null){

                    wayTo.put(edge.target, edge);
                    distanceTo.put(edge.target, newlyDistance);

                }else{

                    double currentDistance = distanceTo.get( edge.target );

                    if(currentDistance > newlyDistance){

                        wayTo.put(edge.target, edge);
                        distanceTo.put(edge.target, newlyDistance);
                        finishedNodes.remove(edge.target);

                    }else if(currentDistance == newlyDistance){

                        if(Math.random() > 0.35){
                            wayTo.put(edge.target, edge);
                        }

                    }

                }

                if(edge.target == target){
                    //points.clear();
                }

            });

        }

        if(wayTo.get(target) == null){
            return null;
        }

        LinkedList<Edge> way = new LinkedList<Edge>();

        Vector2d node = target;

        while(node != start){

            Edge edge = wayTo.get(node);

            way.addFirst( edge );

            node = edge.start;
        }

        return way;
    }

}
