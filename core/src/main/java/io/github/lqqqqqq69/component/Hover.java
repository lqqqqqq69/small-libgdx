package io.github.lqqqqqq69.component;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Der Hover-Komponent zeigt an, ob eine Entität schwebt
 */
public class Hover implements Component {
    public static final ComponentMapper<Hover> MAPPER = ComponentMapper.getFor(Hover.class);
    
    public Viewport viewport;

    public Hover(Viewport viewport) {
        this.viewport = viewport;
    }

    public Viewport getViewport() {
        return viewport;
    }


}
