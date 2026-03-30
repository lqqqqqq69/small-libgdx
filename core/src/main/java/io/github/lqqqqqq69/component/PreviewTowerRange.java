package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Die PreviewTowerRange-Komponente ist die (angezeigte) Reichweite eines noch nicht platzierten Turms
 */
public class PreviewTowerRange implements Component {
    public static final ComponentMapper<PreviewTowerRange> MAPPER = ComponentMapper.getFor(PreviewTowerRange.class);

    private final Circle range;

    public PreviewTowerRange(Vector2 position, float radius) {
        this.range = new Circle(position.x, position.y, radius);
    }

    public Circle getRange() {
        return range;
    }

    public void setCenter(float x, float y) {
        this.range.setPosition(x, y);
    }

    public Vector2 getPosition() {
        return new Vector2(range.x, range.y);
    }
}