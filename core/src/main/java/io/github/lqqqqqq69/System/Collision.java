package io.github.lqqqqqq69.System;

import com.badlogic.gdx.math.Intersector;

import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.TowerRange;

/**
 * Collision enthält Methoden um zu testen, ob
 * - Hitboxen sich überlappen
 * - eine TowerRange sich mit einer Hitbox überlappt
 */
public class Collision {
    
    public static boolean overlaps(Hitbox a, Hitbox b) {
        return a.getBounds().overlaps(b.getBounds());
    }

    public static boolean overlaps(TowerRange a, Hitbox b) {
        return Intersector.overlaps(a.getRange(), b.getBounds());
    }
}
