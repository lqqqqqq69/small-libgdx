package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * Der ShootingDate-Komponent beinhaltet die Daten eines Turmes bezogen auf seine "Angriffe"
 */
public class ShootingData implements Component{
    public static final ComponentMapper<ShootingData> MAPPER = ComponentMapper.getFor(ShootingData.class);

    private float speed;            // Geschwindigkeit des zu produzierenden Projektils
    private float damage;           // Schaden des zu produzierenden Projektils
    private float attackSpeed;      // Angriffsgeschwindigkeit
    private String projectileType;  // Art des zu produzierenden Projektils
    public boolean isCurrentlyShooting = false; // Flag, ob der Turm aktuell schon schießt
    private String effect;          // Effekt des zu produzierenden Projektils

    public ShootingData(float speed, float damage, float attackSpeed, String projectileType, String effect) {
        this.speed = speed;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.projectileType = projectileType;
        this.effect = effect;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDamage() {
        return damage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public String getProjectileType() {
        return projectileType;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setProjectileType(String projectileType) {
        this.projectileType = projectileType;
    }

    public String geteffect() {
        return effect;
    }

}
