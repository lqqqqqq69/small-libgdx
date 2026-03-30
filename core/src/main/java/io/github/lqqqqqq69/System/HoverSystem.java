package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.github.lqqqqqq69.component.Graphic;
import io.github.lqqqqqq69.component.Hover;
import io.github.lqqqqqq69.component.Transform;

/**
 * HoverSystem aktualisiert die Position der Entitaeten mit Hover-Komponente entsprechend mit der Position der Maus
 */
public class HoverSystem extends IteratingSystem{

    public HoverSystem() {
        super(Family.all(Hover.class).get());
    }

    /**
     * processEntity aktualisiert die Position der hovernden Entity entsprechend der Position der Maus
     * So lange eine Enitaet hovert ist sie leicht transparent 
     * 
     * @param entity akutuelle Entitaet
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        Hover hover = Hover.MAPPER.get(entity);
        hover.getViewport().unproject(mousePos);

        Transform transform = Transform.MAPPER.get(entity);
    
        Vector2 position = transform.getPosition();
        Graphic graphic = Graphic.MAPPER.get(entity);

        if (graphic != null) { 
            // Grafik leicht transparent, wenn sie noch hovert
            graphic.getColor().a = 0.5f;
            position.set(
                mousePos.x,
                mousePos.y
            );    
        } else {
            position.set(
                mousePos.x,
                mousePos.y
            );    
        }
    }

}
