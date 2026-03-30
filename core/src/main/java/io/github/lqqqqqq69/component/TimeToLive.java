package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * Der TimeToLive-Komponent beinhaltet die Zeit, bis eine Entität aus der Engine entfernt wird.
 * Beispielsweise werden Projektile entfernt, wenn sie zu lange existieren (weil sie verfehlt haben)
 */
public class TimeToLive implements Component {

    public static final ComponentMapper<TimeToLive> MAPPER = ComponentMapper.getFor(TimeToLive.class);

    private float timeToLive;

    public TimeToLive(float timeToLive){
        this.timeToLive = timeToLive;
    }
    
    public float getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(float timeToLive) {
        this.timeToLive = timeToLive;
    }

}
