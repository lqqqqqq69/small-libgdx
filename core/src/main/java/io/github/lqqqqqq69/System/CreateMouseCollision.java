package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Hover;
import io.github.lqqqqqq69.component.Transform;

/**
 * CreateMousCollision erstellt eine Kollision fuer den Mauszeiger
 * Die erstellte Kollision dient dazu um andere Tuerme auszuwaehlen
 */
public class CreateMouseCollision { 
    private final Engine engine;
    private final Entity mouse;

    /**
     * Der Konstruktor dient fuer die Erstellung der Maus-Kollision
     * 
     * @param engine  
     * @param viewport Viewport des GameScreens
     */
    public CreateMouseCollision(Engine engine, Viewport viewport){
        this.engine = engine;
        mouse = this.engine.createEntity();

        mouse.add(new Transform(
            new Vector2(0, 0),   
            1000,                       // damit Maus immer oben liegt -> macht aber per se in der Funktionalitaet keinen Unterschied
            new Vector2(20f, 20f),
            new Vector2(1f, 1f), 
            0f,                
            0f             
        ));

        mouse.add(new Hitbox(new Vector2(), 0.1f, 0.1f, Hitbox.BoxType.MOUSE));
        
        // Hover -> kuemmert sich darum, dass die Position der Maus geupdated wird
        mouse.add(new Hover(viewport));

        this.engine.addEntity(mouse);
    }

    public Entity getMouse() {
        return mouse;
    }
}
