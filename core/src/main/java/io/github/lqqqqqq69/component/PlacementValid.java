package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * Der PlacementValid-Komponent beinhaltet, ob ein noch nicht platzierte Turm in der aktuellen Situation platziert werden kann
 */
public class PlacementValid implements Component{
    public static final ComponentMapper<PlacementValid> MAPPER = ComponentMapper.getFor(PlacementValid.class);
    private boolean valid;

    public PlacementValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    
}
