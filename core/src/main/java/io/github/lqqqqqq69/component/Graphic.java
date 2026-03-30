package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Der Graphic-Komponent enthält die grafischen Daten eine Entität (Art eines Sprites, Farbe)
 */
public class Graphic implements Component {
    public static final ComponentMapper<Graphic> MAPPER = ComponentMapper.getFor(Graphic.class);

    private TextureRegion region; // Bild/ Sprite der Entität
    private final Color color;    // Farbe

    public Graphic(TextureRegion region, Color color) {
        this.region = region;
        this.color = color;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
    }
}
