package io.github.lqqqqqq69.component;
import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

/**
 * Der Move-Komponent beinhaltet Daten einer Entität die sich bewegen können soll
 */
public class Move implements Component {
    public static final ComponentMapper<Move> MAPPER = ComponentMapper.getFor(Move.class);

    private Vector2 direction;              // Bewegungsrichtung
    private float speed;                    // Bewegungsgeschwindigkeit
    public boolean newAnimation = false;    // Relevant für CastleCollision, damit Angriffsanimation ordentlich ausgeführt wird
    private boolean modifiedMovement = false;   // Hat der Gegner durch Effekte modifziertes Movement?
    private float modifiedTimer = 0;        // Timer für das modifizierte Movement

    public Move(float speed) {
        this.direction = new Vector2();
        this.speed = speed;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isModifiedMovement() {
        return modifiedMovement;
    }
    
    /**
     * setModifiedMovement setzt den ModifiedMovement-Flag und einen Standardwert für die Dauer
     * @param modifiedMovement
     */
    public void setModifiedMovement(boolean modifiedMovement) {
        this.modifiedMovement = modifiedMovement;
        this.modifiedTimer = 2;
    }

    public float getModifiedTimer() {
        return modifiedTimer;
    }

    public void setModifiedTimer(float modifiedTimer) {
        this.modifiedTimer = modifiedTimer;
    }

}
