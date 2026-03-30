package io.github.lqqqqqq69.System;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.AtlasAsset;
import io.github.lqqqqqq69.component.Animation2D;
import io.github.lqqqqqq69.component.Animation2D.AnimationType;
import io.github.lqqqqqq69.component.Facing;
import io.github.lqqqqqq69.component.Facing.FacingDirection;
import io.github.lqqqqqq69.component.Graphic;

/**
 * AnimationSystem verwaltet den Fortschritt der Animationen und aktualiset sie
 */
public class AnimationSystem extends IteratingSystem {
    private static final float FRAME_DURATION = 0.1f;       // Dauer eines Animationsframes in Sekunden
    private final AssetService assetService;
    private final Map<CacheKey, Animation<TextureRegion>> animationsCache; // Animationscache

    public AnimationSystem(AssetService assetService) {
        super(Family.all(Animation2D.class, Graphic.class, Facing.class).get());
        this.assetService = assetService;
        this.animationsCache = new HashMap<>();
    }
    /**
     * processEntity aktualisiert die Animation
     * 
     * - Falls Animation nicht mehr zur Entität passt -> neue Art von Animation setzen + Reset Statetime auf 0
     * - Sonst Statime um deltaTime erhöhen
     * - Setzen der Animation auf den aktuell "berechneten" Keyframe
     * 
     * @param entity
     * @param deltaTime Zeit seit letztem Frame
     */
    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Animation2D animation2D = Animation2D.MAPPER.get(entity);
        FacingDirection facingDirection = Facing.MAPPER.get(entity).getDirection();
        final float stateTime;
        if (animation2D.isDirty() || facingDirection != animation2D.getDirection()) {
            updateAnimation(animation2D, facingDirection);
            stateTime = 0f;
        } else {
            stateTime = animation2D.incAndGetStateTime(deltaTime);
        }

        Animation<TextureRegion> animation = animation2D.getAnimation();
        animation.setPlayMode(animation2D.getPlayMode());
        TextureRegion keyFrame = animation.getKeyFrame(stateTime);
        Graphic.MAPPER.get(entity).setRegion(keyFrame);
    }

    /**
     * updateAnimation aktualisiert den Animationstyp
     * 
     * - Erstellen Cachekey
     * - Gibt es den vollständigen Cachekey schon von vorher im AnimationsCache?
     *  - Falls nein - Speicherung neue Art von von Animation im Cache
     *  (- Falls ja - Nutzung alter Animation aus dem Cache)
     * - Setzung der Animation mit Hilfe des Cachekeys
     * 
     * @param animation2D
     * @param facingDirection neue Bewegungsrichtung
     */
    private void updateAnimation(Animation2D animation2D, FacingDirection facingDirection) {
        AtlasAsset atlasAsset = animation2D.getAtlasAsset();
        String atlasKey = animation2D.getAtlasKey();
        AnimationType type = animation2D.getType();
        CacheKey cacheKey = new CacheKey(atlasAsset, atlasKey, type, facingDirection);
        
        
        Animation<TextureRegion> animation = animationsCache.computeIfAbsent(cacheKey, key -> {
            //Erstellung neuer Cachekey, wenn der Cachekey noch nicht existent
            TextureAtlas textureAtlas = this.assetService.get(atlasAsset);
            String AnimTypeStr = "";
            switch (type) {
                case WALK -> AnimTypeStr = "/w_";
                case ATTACK -> AnimTypeStr = "/a_";
                case DEATH -> AnimTypeStr = "/d_";
                case IDLE -> AnimTypeStr = "/s_";
            }
            String combinedKey = atlasKey + AnimTypeStr + facingDirection.getAtlasKey();
            Array<AtlasRegion> regions = textureAtlas.findRegions(combinedKey);
            if(regions.isEmpty()){
                throw new IllegalArgumentException("No regions found for key: " + combinedKey);
            }
            return new Animation<>(FRAME_DURATION, regions);
        });
        
        // Setzen Animation
        animation2D.setAnimation(animation, facingDirection);
        
    }

    public record CacheKey(
        AtlasAsset atlasAsset,
        String atlasKey, 
        AnimationType animationType, 
        FacingDirection direction) {
        
    }

}
