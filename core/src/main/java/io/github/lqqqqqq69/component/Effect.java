package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * Der Effect-Komponent speichert den Effekt eines Projektils (falls ein Projektil einen Effekt besitzt)
 */
public class Effect implements Component {
    public static final ComponentMapper<Effect> MAPPER = ComponentMapper.getFor(Effect.class);

    private String effect;

    public Effect(String effect) {
        this.effect = effect;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
}
