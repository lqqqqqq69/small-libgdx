package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Die TowerRange-Komponente ist die Reichweite eines platzierten Turms
 */
public class TowerRange implements Component {
    public static final ComponentMapper<TowerRange> MAPPER = ComponentMapper.getFor(TowerRange.class);

    private final Circle range;

    public TowerRange(Vector2 position, float radius) {
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
