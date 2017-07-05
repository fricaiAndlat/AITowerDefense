package de.diavololoop.game.map;

import de.diavololoop.game.Resource;
import de.diavololoop.game.effect.Effect;
import de.diavololoop.game.entitys.Enemy;
import de.diavololoop.game.entitys.Entity;
import de.diavololoop.game.map.turret.FlashTurret;
import de.diavololoop.game.map.turret.GunTurret;
import de.diavololoop.game.map.turret.LaserTurret;
import de.diavololoop.game.map.turret.Turret;
import de.diavololoop.game.pathfinding.Edge;
import de.diavololoop.game.pathfinding.Graph;
import de.diavololoop.gui.Animation;
import de.diavololoop.gui.GraphicUserInterface;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gast2 on 20.06.17.
 */
public class Map extends Graph{

    private final static int FIELD_WIDTH = 16;
    private final static int FIELD_HEIGHT = 16;

    private final static double DRAW_ZOOM = 1/10d;



    //graph stuff
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private Vector2d[][][] nodes;
    private Vector2d spawnPoint;
    private Vector2d targetPoint;

    //debug
    private boolean debug = false;
    private boolean help = false;

    private ArrayList<Entity> entitys = new ArrayList<Entity>();
    private LinkedList<Effect> effects = new LinkedList<Effect>();

    private Field[][] fields = new Field[  FIELD_WIDTH  ][  FIELD_HEIGHT  ];

    private Field spawn, base;

    private Thread tickThread;

    List<Edge> way;

    public Map() {

        initFields();
        createGraph();

        System.out.println("edges: "+edges.size());

        way = this.getWay(spawnPoint, targetPoint);

        tickThread = new Thread(() -> {while(true){onTick(1/120d);}});
        tickThread.setDaemon(true);
        tickThread.start();
    }

    private Animation.AnimationIterator iter;
    private int counter = 0;

    private void onTick(double dt){

        synchronized(entitys){
            entitys.removeIf(entity -> !entity.isAlive());
            entitys.forEach(entity -> entity.gameTick(dt));
        }


        if(++counter > 40){
            counter = 0;
            synchronized(entitys) {
                entitys.add(new Enemy(this, spawnPoint, targetPoint));
                entitys.removeIf(entity -> !entity.isAlive());
            }
            synchronized(effects) {
                effects.removeIf(effect -> !effect.isAlive());
            }
        }



        for(int j = 0; j < FIELD_HEIGHT; ++j){
            for(int i = 0; i < FIELD_WIDTH; ++i) {
                fields[i][j].onTick(dt);
            }
        }

        try {
            Thread.sleep(1000/120);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void draw(GraphicUserInterface<?> handle){



        for(int j = 0; j < FIELD_HEIGHT; ++j){
            for(int i = 0; i < FIELD_WIDTH; ++i){

                fields[i][j].onDraw(handle, DRAW_ZOOM);

                /*handle.drawImage(
                        (float)((i - FIELD_WIDTH/2) * DRAW_ZOOM) + (float)DRAW_ZOOM/2, (float)((j - FIELD_HEIGHT/2) * DRAW_ZOOM) + (float)DRAW_ZOOM/2,
                        (float)DRAW_ZOOM, (float)DRAW_ZOOM,
                        fields[i][j].getImage());*/

                Turret turret = fields[i][j].getTurret();

                if(turret == null || turret.health >= turret.maxHealth){
                    continue;
                }



                if(turret.health < turret.maxHealth){
                    handle.setColor((float)(1-turret.health/turret.maxHealth), (float)(turret.health/turret.maxHealth), 0);
                    handle.fillRect(
                            (float)((i - FIELD_WIDTH/2) * DRAW_ZOOM) + (float)DRAW_ZOOM/2,
                            (float)(0.4*DRAW_ZOOM + (j - FIELD_HEIGHT/2) * DRAW_ZOOM) + (float)DRAW_ZOOM/2,
                            (float)(turret.health/turret.maxHealth * 0.8*DRAW_ZOOM),
                            (float)(0.1*DRAW_ZOOM));

                    handle.drawRect(
                            (float)((i - FIELD_WIDTH/2) * DRAW_ZOOM) + (float)DRAW_ZOOM/2, (float)(0.4*DRAW_ZOOM + (j - FIELD_HEIGHT/2) * DRAW_ZOOM) + (float)DRAW_ZOOM/2,
                            (float)(0.8*DRAW_ZOOM), (float)(0.1*DRAW_ZOOM));
                }




            }
        }

        if(debug){
            handle.setColor(0, 0, 1);
            edges.forEach(edge -> printEdge(handle, edge));
        }



        if(help){
            handle.setColor(1, 0, 0);
            way.forEach(edge -> printEdge(handle, edge));
        }
        synchronized(entitys) {
            entitys.forEach(entity -> handle.drawImage(
                    (float) (entity.getX() * DRAW_ZOOM), (float) (entity.getY() * DRAW_ZOOM),
                    (float) (entity.getWidth() * DRAW_ZOOM), (float) (entity.getHeight() * DRAW_ZOOM),
                    entity.getImage()));
        }

        synchronized(effects) {
            effects.forEach(effect -> effect.draw(handle, DRAW_ZOOM));
        }

    }

    private void initFields() {

        for(int j = 0; j < FIELD_HEIGHT; ++j){
            for(int i = 0; i < FIELD_WIDTH; ++i){
                fields[i][j] = new Field(i, j, FIELD_WIDTH, FIELD_HEIGHT);
            }
        }

        spawn = new CustomField(0, FIELD_HEIGHT/2, FIELD_WIDTH, FIELD_HEIGHT, Resource.current().image("spawn"));
        base = new CustomField(FIELD_WIDTH-1, FIELD_HEIGHT/2, FIELD_WIDTH, FIELD_HEIGHT, Resource.current().image("base"));


        fields[0][ FIELD_HEIGHT/2 ] = spawn;
        fields[ FIELD_WIDTH-1 ][ FIELD_HEIGHT/2 ] = base;

        for(int i = 2; i < 16; ++i){
            fields[3][i].setTurret(new LaserTurret(this));
        }

        for(int i = 4; i < 15; ++i){
            fields[i][2].setTurret(new FlashTurret(this));
        }

        for(int i = 6; i < 16; ++i){
            fields[i][5].setTurret(new GunTurret(this));
        }

        for(int i = 6; i < 15; ++i){
            fields[6][i].setTurret(new Turret(Resource.current().image("turret"), this));
        }
        for(int i = 7; i < 16; ++i){
            fields[9][i].setTurret(new Turret(Resource.current().image("turret"), this));
        }
        for(int i = 6; i < 15; ++i){
            fields[12][i].setTurret(new Turret(Resource.current().image("turret"), this));
        }

    }

    private void createGraph(){
        nodes = new Vector2d[ FIELD_WIDTH ][ FIELD_HEIGHT ][ 3 ]; // [x position][y position][pos] pos: 0=top, 1=right, 2=center

        for(int j = 0; j < FIELD_HEIGHT; ++j){
            for(int i = 0; i < FIELD_WIDTH; ++i){

                nodes[i][j][0] = new Vector2d( i - FIELD_WIDTH/2 + 0.5, j - FIELD_HEIGHT/2 + 1.0 );
                nodes[i][j][1] = new Vector2d( i - FIELD_WIDTH/2 + 1.0, j - FIELD_HEIGHT/2 + 0.5 );
                nodes[i][j][2] = new Vector2d( i - FIELD_WIDTH/2 + 0.5, j - FIELD_HEIGHT/2 + 0.5 );


            }
        }

        spawnPoint = nodes[0][FIELD_HEIGHT/2][2];
        targetPoint = nodes[FIELD_WIDTH-1][FIELD_HEIGHT/2][2];


        Vector2d[][][] n = nodes;

        edges.clear();

        for(int j = 0; j < FIELD_HEIGHT; ++j){
            for(int i = 0; i < FIELD_WIDTH; ++i){

                Field field = fields[i][j];

                if(field.isBlocked()){
                    continue;
                }

                //top - center
                edges.add(new Edge(n[i][j][0], n[i][j][2], field.getSpeedMultiplier())); //bot   - center
                edges.add(new Edge(n[i][j][1], n[i][j][2], field.getSpeedMultiplier())); //right - center
                edges.add(new Edge(n[i][j][0], n[i][j][1], field.getSpeedMultiplier())); //bot   - right

                if(i != 0){
                    //not left side
                    edges.add(new Edge(n[i][j][2], n[i - 1][j][1], field.getSpeedMultiplier())); //center - left
                    edges.add(new Edge(n[i][j][0], n[i - 1][j][1], field.getSpeedMultiplier())); //center - left
                }

                if(j != 0){
                    //not bottom side
                    edges.add(new Edge(n[i][j][2], n[i][j - 1][0], field.getSpeedMultiplier())); //center - bottom
                    edges.add(new Edge(n[i][j][1], n[i][j - 1][0], field.getSpeedMultiplier())); //center - bottom
                }

                if(i != 0 && j != 0){
                    //not left side and not bottom
                    edges.add(new Edge(n[i][j - 1][0], n[i - 1][j][1], field.getSpeedMultiplier())); //bottom - right
                }

            }
        }

        edges.addAll(edges.stream().map(edge -> new Edge(edge.target, edge.start, edge.modifierSpeed)).collect(Collectors.toList()));


    }

    private void printEdge(GraphicUserInterface handle, Edge edge){

        float x0 = (float)edge.start.x;
        float y0 = (float)edge.start.y;

        float dx = (float)(edge.target.x - x0);
        float dy = (float)(edge.target.y - y0);

        int l = 4;

        handle.setColor((float)(edge.modifierSpeed*edge.length)/(10*20), 0, 0);

        for(int i = 0; i < l; ++i){
            handle.fillOval((x0 + dx*i/l) * (float)DRAW_ZOOM, (y0 + dy*i/l) * (float)DRAW_ZOOM, 0.007f, 0.007f);
        }

    }


    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    public void toggleDebug() {
        debug = !debug;
    }

    public void toggleHelp() {
        help = !help;
    }

    public Turret intersectTurret(double x, double y, double width, double height) {

        for(int j = 0; j < FIELD_HEIGHT; ++j){
            for(int i = 0; i < FIELD_WIDTH; ++i){


                Turret turret = fields[i][j].getTurret();
                if(turret != null){

                    if( Math.abs(i - FIELD_WIDTH/2 + 0.5 - x) < width/2 + 0.5 && Math.abs(j - FIELD_HEIGHT/2 + 0.5 - y) < height/2 + 0.5){
                        return turret;
                    }
                }

            }
        }

        return null;
    }

    public void onTowerDestroyed(Turret turret) {

        createGraph();

        System.out.println("tower destroyed");

    }

    private long lastShowWay = 0;
    public void showWay(double x, double y) {

        if(System.currentTimeMillis() - lastShowWay < 200){
            return;
        }
        lastShowWay = System.currentTimeMillis();
        Vector2d pos = new Vector2d(x/DRAW_ZOOM, y/DRAW_ZOOM);

        Vector2d nearest = nodes[0][0][0];
        double dist = nearest.distance(pos);

        for(int j = 0; j < FIELD_HEIGHT; ++j) {
            for (int i = 0; i < FIELD_WIDTH; ++i) {
                for(int k = 0; k < 3; ++k){

                    if(nodes[i][j][k].distance(pos) < dist){
                        dist = nodes[i][j][k].distance(pos);
                        nearest = nodes[i][j][k];
                    }

                }
            }
        }

        way = this.getWay(nearest, targetPoint);

    }

    public List<Entity> getEntitys() {
        return entitys;
    }

    public void addEffect(Effect effect) {
        synchronized(effects) {
            effects.add(effect);
        }
    }

    public double getZoom() {
        return DRAW_ZOOM;
    }

    public void addDeath(double x, double y, double value) {

        int tileX = (int)(x + FIELD_WIDTH/2 - 0.5);
        int tileY = (int)(y + FIELD_HEIGHT/2 -0.5);

        //(float)((i - FIELD_WIDTH/2) * DRAW_ZOOM) + (float)DRAW_ZOOM/2, (float)((j - FIELD_HEIGHT/2) * DRAW_ZOOM) + (float)DRAW_ZOOM/2

        //System.out.printf("death @ %d/%d\r\n", tileX, tileY);

        fields[tileX][tileY].addDeath(value);

        createGraph();
    }
}
