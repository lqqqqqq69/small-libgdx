package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

/**
 * Der Troop-Komponente verweist auf eine zu einem Turm gehörende Truppe (nötig für Upgrade oder Entfernung eines Turms)
 */
public class Troop implements Component {
    public static final ComponentMapper<Troop> MAPPER = ComponentMapper.getFor(Troop.class);

    private Entity troop;
    
    public Troop(Entity troop) {
        this.troop = troop;
    }

    public Entity getTroop(){
        return this.troop;
    }

}
