package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * additionalEnemyData ist ein Komponent, welcher die Art eines Gegners und dessen Reward enthält.
 * Zusätzlich dient er auch zur Unterscheidung zwischen Gegnern und anderen Entitäten
 */

public class additionalEnemyData implements Component {
    public static final ComponentMapper<additionalEnemyData> MAPPER = ComponentMapper.getFor(additionalEnemyData.class);

    private int reward;         // Reward für den Gegner
    private String enemyType;   // Art des Gegners

    public additionalEnemyData(String enemyType, int reward) {
        this.reward = reward;
        this.enemyType = enemyType;
    }

    public int getReward() {
        return reward;
    }

    public String getEnemyType() {
        return enemyType;
    }

 

    

}
