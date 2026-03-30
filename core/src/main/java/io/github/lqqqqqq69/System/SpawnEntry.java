package io.github.lqqqqqq69.System;

import com.badlogic.gdx.math.MathUtils;

/**
 * SpawnEntry definiert einen Spawn-Eintrag als Teil ein Welle
 */
public class SpawnEntry {
    private String enemyType;   // Art eines Spawn-Eintrags
    private int count;          // Anzahl der zu erzeugenden Gegner
    private float delay;        // Delay zum vorherigen Eintrag
    private float intervalMin;  // Mindestzeit zwischen Spawns eines Eintrags
    private float intervalMax;  // Maximalzeit zwischen Spawns eines Eintrags

    public SpawnEntry(String enemyType, int minCount, int maxCount, float delay, float intervalMin, float intervalMax) {
        this.enemyType = enemyType;
        this.count = MathUtils.random(minCount, maxCount);
        this.delay = delay;
        this.intervalMin = intervalMin;
        this.intervalMax = intervalMax; 
    }

    public String getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(String enemyType) {
        this.enemyType = enemyType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getIntervalMin() {
        return intervalMin;
    }

    public void setIntervalMin(float intervalMin) {
        this.intervalMin = intervalMin;
    }

    public float getIntervalMax() {
        return intervalMax;
    }

    public void setIntervalMax(float intervalMax) {
        this.intervalMax = intervalMax;
    } 
}
