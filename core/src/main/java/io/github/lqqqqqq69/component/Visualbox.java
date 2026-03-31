package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * 
 */
public class Visualbox implements Component {
    public static final ComponentMapper<Visualbox> MAPPER = ComponentMapper.getFor(Visualbox.class);

    private Rectangle bounds;       // Visualbox

    /**
     * Der Konstruktor setzt die Visualbox
     * 
     * @param center  Mittelpunkt der Visualbox
     * @param width Breite
     * @param height Höhe
     */
    public Visualbox(Vector2 center, float width, float height) { 
        this.bounds = new Rectangle(
            center.x - width / 2f,
            center.y - height / 2f,
            width,
            height
        );

    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setSize(float x, float y){
        this.bounds.x = x;
        this.bounds.y = y;
    }

    public void setCenter(Vector2 center) {
        this.bounds.setCenter(center);
    }

    public Vector2 getPosition() {
        return new Vector2(bounds.x + bounds.width / 2f, bounds.y + bounds.height / 2f);
    }
}
