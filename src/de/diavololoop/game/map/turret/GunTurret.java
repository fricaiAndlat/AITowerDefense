package de.diavololoop.game.map.turret;

import de.diavololoop.game.Resource;
import de.diavololoop.game.effect.LineEffect;
import de.diavololoop.game.entitys.Enemy;
import de.diavololoop.game.entitys.Entity;
import de.diavololoop.game.map.Map;
import org.joml.Vector2d;

/**
 * Created by gast2 on 27.06.17.
 */
public class GunTurret extends Turret{

    private final static Vector2d HORIZONTAL = new Vector2d(1, 0);

    private final static double ENERGY_PER_SHOT = 2;
    private final static double ENERGY_MAX = 2.2;
    private final static double ENERGY_REGENERATION = 20;
    private final static double DAMAGE_PER_SHOT = 5;
    private final static double RANGE_MAX = 1.8;

    private double energy = ENERGY_MAX;

    public GunTurret(Map map) {
        super(Resource.current().image("gunturret"), map);
    }

    @Override
    public void onTick(double dt){

        energy += ENERGY_REGENERATION * dt;
        energy = Math.min(ENERGY_MAX, energy);

        if(energy >= ENERGY_PER_SHOT){
            shoot();
        }

    }

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

        energy -= ENERGY_PER_SHOT;
        nearest.damage(DAMAGE_PER_SHOT);

        map.addEffect(new LineEffect(Resource.current().animation("gunshot"), position, nearest.getPosition(), 0.4 * map.getZoom()));




    }


}
