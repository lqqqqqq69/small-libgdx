package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;

import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.asset.SoundAsset;
import io.github.lqqqqqq69.component.Animation2D;
import io.github.lqqqqqq69.component.Damage;
import io.github.lqqqqqq69.component.Health;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Move;
import io.github.lqqqqqq69.component.additionalEnemyData;

/**
 * HealthSystem testet, ob ein Gegner 0 HP hat und dementsprechend "stirbt"
 */
public class HealthSystem extends IteratingSystem{
    private final Engine engine;
    private final CashSystem cashSystem;
    private final WaveSystem waveSystem;
    private final AudioService audioService;
    
    public HealthSystem(Engine engine, CashSystem cashSystem, WaveSystem waveSystem, AudioService audioService) {
        super(Family.all(Health.class, Damage.class).get());
        this.engine = engine;
        this.cashSystem = cashSystem;
        this.waveSystem = waveSystem;
        this.audioService = audioService;
    }

    /**
     * processEntity testet pro Frame fuer alle Gegner-Entitaeten, ob diese 0 HP haben
     * Falls ja: enemyDeath
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Health health = Health.MAPPER.get(entity);
        if (health.getHealth() <= 0){
            enemyDeath(entity);
        }
    }

    /**
     * enemyDeath fuehrt die Todesanimation eines Gegners durch
     * 
     * - Test, ob ein Gegner schon in der Todesanimation ist
     *  - Falls nein:
     *      - Entfernen Gegner aus der Liste der aktiven Gegner
     *      - Geldstand anpassen
     *      - Entfernen der Entity Hitbox (damit er nicht noch zufaellig getroffen wird)
     *      - Movement Speed auf 0 setzen, Todesanimation abspielen
     *      - Falls gestorbener Gegner = Golem -> Abspielen Todessound
     * 
     * - Sonst, wenn Todesanimation fertig -> Entfernung Gegner aus Engine
     * 
     * @param entity
     */
    public void enemyDeath(Entity entity){
        
        Move move = Move.MAPPER.get(entity);
        Animation2D animation = Animation2D.MAPPER.get(entity);
        additionalEnemyData enemy = additionalEnemyData.MAPPER.get(entity);
                    
        if (!move.newAnimation) {
            waveSystem.getActiveEnemies().remove(entity);              // Gegner aus der Liste er aktiven Gegner entfernen
            cashSystem.setCashAmount(cashSystem.getCashAmount() + enemy.getReward());   // Geld aktualisieren
            entity.remove(Hitbox.class);                // Entfernen der Hitbox, wenn der Gegner stirbt -> so kann ein toter Gegner nicht nochmal getroffen werden
            move.setSpeed(0);                                   
            
            // Todesanimation
            animation.setType(Animation2D.AnimationType.DEATH); 
            animation.setPlayMode(Animation.PlayMode.NORMAL);
            move.newAnimation = true; 
            if(enemy.getEnemyType().equals("Golem")){
                // Todessound bei Golem
                audioService.playSound(SoundAsset.GOLEM_DEATH);
            }
                    
        } else {
            if(animation.isFinished()){
                getEngine().removeEntity(entity); // Entferne den Gegner nach dem Angriff
            }  
            
}
    }
}
