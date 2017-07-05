package de.diavololoop.game.map.turret;

/**
 * Created by Chloroplast on 04.07.2017.
 */

import de.diavololoop.game.Resource;
import de.diavololoop.game.effect.Effect;
import de.diavololoop.game.effect.LineEffect;
import de.diavololoop.game.entitys.Enemy;
import de.diavololoop.game.entitys.Entity;
import de.diavololoop.game.map.Map;
import org.joml.Vector2d;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * Created by gast2 on 27.06.17.
 */
public class FlashTurret extends Turret{

    private final static Vector2d HORIZONTAL = new Vector2d(1, 0);

    private final static double DAMAGE_PER_SECOND_MAX = 40;
    private final static double RANGE_MAX = 2.1;

    private HashMap<Enemy, LineEffect> effects = new HashMap<Enemy, LineEffect>();
    private LinkedList<Enemy> enemysToBeRemoved = new LinkedList<Enemy>();

    public FlashTurret(Map map) {
        super(Resource.current().image("flashturret"), map);
    }

    @Override
    public void onTick(double dt){

        if(super.health > 0){
            shoot(dt);
        }else{

            if(!effects.isEmpty()){

                effects.values().forEach(effect -> effect.isAlive = false);
                effects.clear();

            }

        }


    }

    private void shoot(double dt) {


        for(Entity entity: map.getEntitys()){
            if(!(entity instanceof Enemy)){
                continue;
            }

            Enemy enemy = (Enemy)entity;

            if(enemy.distanceTo(position) > RANGE_MAX){

                if(effects.containsKey(enemy)){
                   effects.get(enemy).isAlive = false;
                   effects.remove(enemy);
                }

            }else{

                if(!effects.containsKey(enemy)){
                    LineEffect shotEffect = new LineEffect(Resource.current().animation("flashshot"), position, enemy.getPosition(), 0.4 * map.getZoom());
                    map.addEffect(shotEffect);
                    effects.put(enemy, shotEffect);
                }

            }
        }

        effects.keySet().forEach(enemy -> {
            enemy.damage( DAMAGE_PER_SECOND_MAX * dt / effects.size());
            if(!enemy.isAlive()){
                effects.get(enemy).isAlive = false;
                enemysToBeRemoved.add(enemy);
            }
        });

        enemysToBeRemoved.forEach(enemy -> effects.remove(enemy));
        enemysToBeRemoved.clear();



    }


}

