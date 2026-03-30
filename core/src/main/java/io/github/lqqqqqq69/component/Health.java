package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;


/**
 * Der Health-Komponent speichert die HP einer Entität und weitere Werte im Bezug auf genommenen Schaden
 */
public class Health implements Component{
    public static final ComponentMapper<Health> MAPPER = ComponentMapper.getFor(Health.class);

    private float health;       // aktuelle HP einer Entität
    private float maxHealth;    // maximale HP einer Entität (relevant für Tür Entität)
    private float armor;        // Armor (für mögliche Schadensreduktion)
    private boolean magic_resistance;   // Resistenz gegen Magie

    public Health (float health, float armor, boolean magic_resistance){
        this.maxHealth = health;
        this.health = health;
        this.armor = armor;
        this.magic_resistance = magic_resistance;
    }

    /**
     * applyDamage wendet Schaden auf die aktuellen HP einer Entity an
     * 
     * - Falls der eingehende Schaden durch Magie entsteht und die Entität Magieresistenz besitzt -> Schadenreduktion auf 1
     * - Falls der eingehende Schaden kleiner oder gleich der Armor des Gegner -> Schadensreduktion auf 1
     * - HP der Entität werden auf das entweder auf HP-incomingDamage oder 0 gesetzt. je nach dem was höher ist
     * 
     * @param incomingDamage einegehnder Schaden
     * @param magic kommt der eingehende Schaden durch Magie
     */
    public void applyDamage(float incomingDamage, boolean magic) {
        if (magic && magic_resistance) incomingDamage = 1;
        if (armor >= incomingDamage) incomingDamage = 1;

        this.health = Math.max(0, this.health-incomingDamage);
    }

    public float getHealth() {
        return health;
    }

    public float getArmor() {
        return armor;
    }
    
    public float getMaxHealth() {
        return maxHealth;
    }

}
