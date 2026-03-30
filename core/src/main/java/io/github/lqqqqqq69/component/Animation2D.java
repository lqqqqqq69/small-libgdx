package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.lqqqqqq69.asset.AtlasAsset;
import io.github.lqqqqqq69.component.Facing.FacingDirection;

/**
 * Animation2d beinhaltet Daten zur Animation und dessen Fortschritt
 */
public class Animation2D implements Component {
    public static final ComponentMapper<Animation2D> MAPPER = ComponentMapper.getFor(Animation2D.class);

    private final AtlasAsset atlasAsset;    // Atlas, in welchem die Assets für die einzelnen Frames zu finden sind
    private final String atlasKey;          // Basisname im Atlas 
    private AnimationType type;             // Art der Animation (walk, death etc)
    private FacingDirection direction;      // Richtung in die Entität gedreht ist
    private PlayMode playMode;              // wie die Animation abgespielt wird (z.B. LOOP)
    private float speed;                    // Animationsgeschwindigkeit
    private float stateTime;                // Zeit in der aktuellen Animation         
    private Animation<TextureRegion> animation; 
    private boolean dirty;                  // passt aktuelle Animation zu Entität?

    public Animation2D(AtlasAsset atlasAsset, String atlasKey, AnimationType type, PlayMode playMode, float speed) {
        this.atlasAsset = atlasAsset;
        this.atlasKey = atlasKey;
        this.type = type;
        this.playMode = playMode;
        this.speed = speed;
        this.stateTime = 0f;
        this.animation = null;
        this.dirty = true;
    }
    /**
     * setAnimation setzt eine Animation samt Richtung
     * @param animation Animation
     * @param direction Richtung
     */
    public void setAnimation(Animation<TextureRegion> animation, FacingDirection direction) {
        this.animation = animation;
        this.stateTime = 0f;
        this.direction = direction;
        this.dirty = false;
    }

    public FacingDirection getDirection() {
        return direction;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public AtlasAsset getAtlasAsset() {
        return atlasAsset;
    }

    public String getAtlasKey() {
        return atlasKey;
    }

    /**
     * setType ändert den Animationstype und setzt dirty auf true (damit die Animation im nächsten Frame angepasst wird)
     * @param type neue Art der Animation
     */
    public void setType(AnimationType type) {
        this.type = type;
        this.dirty = true;
    }

    public AnimationType getType() {
        return type;
    }

    public PlayMode getPlayMode() {
        return playMode;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    public boolean isDirty() {
        return dirty;
    }
    /**
     * incAndGetStateTime erhöht die Zeit im aktuellen State um die 
     * Zeit seit den letztem Frame Mal der Animationsgeschwindigkeit und gibt sie aus
     * @param deltaTime Zeit seit letzten Frame 
     * @return erhöhte Statetime
     */
    public float incAndGetStateTime(float deltaTime) {
        this.stateTime += deltaTime * speed;
        return this.stateTime;
    }

    public boolean isFinished() {
        return animation.isAnimationFinished(stateTime);
    }

    public enum AnimationType {
        WALK,
        ATTACK,
        DEATH,
        OPEN,
        IDLE
        ;

        private final String atlasKey;

        AnimationType() {
            this.atlasKey = this.name().toLowerCase();
        }

        public String getAtlasKey() {
            return atlasKey;
        }
    }
}