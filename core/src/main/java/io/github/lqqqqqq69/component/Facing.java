package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * Der Facing-Komponent zeigt an, in welche Richtung ein Objekt gereht ist
 */
public class Facing implements Component {
    public static final ComponentMapper<Facing> MAPPER = ComponentMapper.getFor(Facing.class);

    private FacingDirection direction;

    public Facing(FacingDirection direction) {
        this.direction = direction;
    }
    
    /**
     * FacingDirection zeigt an in welche Richtung sich eine Entität drehen kann
     * Ausgehend davon erstellt es den AtlasKey
     */
    public enum FacingDirection {
        UP, DOWN, LEFT, RIGHT;

        private final String atlaskey;
    
        FacingDirection() {
            this.atlaskey = name().toLowerCase();
        }

        public String getAtlasKey() {
            return atlaskey;
        }
    
    }
    public FacingDirection getDirection() {
        return direction;
    }

    public void setDirection(FacingDirection direction) {
        this.direction = direction;
    }

}
