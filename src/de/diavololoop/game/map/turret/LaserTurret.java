package de.diavololoop.game.map.turret;

import de.diavololoop.game.Resource;
import de.diavololoop.game.effect.LineEffect;
import de.diavololoop.game.entitys.Enemy;
import de.diavololoop.game.entitys.Entity;
import de.diavololoop.game.map.Map;
import de.diavololoop.gui.GuiImage;
import org.joml.Vector2d;

/**
 * Created by gast2 on 27.06.17.
 */
public class LaserTurret extends Turret{

    private final static Vector2d HORIZONTAL = new Vector2d(1, 0);

    private final static double ENERGY_PER_SHOT = 100;
    private final static double ENERGY_MAX = 120;
    private final static double ENERGY_REGENERATION = 100;
    private final static double DAMAGE_PER_SHOT = 50;
    private final static double RANGE_MAX = 2.1;

    private double energy = ENERGY_MAX;

    private GuiImage[] textures;

    public LaserTurret(Map map) {

        super(null, map);

        textures = Resource.current().mimage("laserturret");


        super.icon = textures[0];
        super.iconSize = 2;
    }

    @Override
    public void onTick(double dt){

        energy += ENERGY_REGENERATION * dt;
        energy = Math.min(ENERGY_MAX, energy);

        shoot();

    }


    private Vector2d tempShoot = new Vector2d();
    private void shoot() {

        Enemy nearest = null;

        for(Entity entity: map.getEntitys()){
            if(!(entity instanceof Enemy)){
                continue;
            }

            Enemy enemy = (Enemy)entity;

            if(nearest == null || enemy.distanceTo(position) < nearest.distanceTo(position)){
                nearest = enemy;
            }
        }

        if(nearest == null || nearest.distanceTo(position) > RANGE_MAX){
            return;
        }

        nearest.getPosition().sub(position, tempShoot);
        double angle = (-HORIZONTAL.angle(tempShoot)) / (2*Math.PI) + 0.5;
        super.icon = textures[  (int)Math.round(angle * 15)  ];


        if(energy >= ENERGY_PER_SHOT){

            energy -= ENERGY_PER_SHOT;
            nearest.damage(DAMAGE_PER_SHOT);

            map.addEffect(new LineEffect(Resource.current().animation("lasershot"), position, nearest.getPosition(), 0.4 * map.getZoom()));

        }





    }


}
