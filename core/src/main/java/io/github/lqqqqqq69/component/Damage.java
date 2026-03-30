package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * Der Damage-Komponent speichert den Schaden einer Entität
 */
public class Damage implements Component {
    public static final ComponentMapper<Damage> MAPPER = ComponentMapper.getFor(Damage.class);

    private float damage;

    public Damage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
