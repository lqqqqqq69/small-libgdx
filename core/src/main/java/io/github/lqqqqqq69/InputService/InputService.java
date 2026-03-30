package io.github.lqqqqqq69.InputService;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * InputService ist eine einfache Implementierung einer Input-Verwaltung um Inputs gegebenfalls zu blockieren
 */
public class InputService extends EntitySystem {
    public boolean blocked = false;

    public boolean MOUSE1 = false;
    public boolean MOUSE2 = false;

    public boolean KEY_SPACE = false;
    public boolean KEY_ESC = false;

    public boolean KEY_1 = false;
    public boolean KEY_2 = false;
    public boolean KEY_3 = false;

    public boolean KEY_ENTER = false;

    public InputService() {}

    /**
     * update testet pro Frame, ob Inputs blockiert sind.
     * Falls nicht, werden die Inputs an die jeweiligen Boolean-Variablen weitergegeben
     * Falls doch, wird nur noch der ESC-Input registriert
     */
    @Override
    public void update(float deltaTime) {
        KEY_ESC = Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);

        if (blocked) return;

        // Maus
        MOUSE1 = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        MOUSE2 = Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT);

        // Tastatur
        KEY_SPACE = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        KEY_ENTER = Gdx.input.isKeyPressed(Input.Keys.ENTER);

        KEY_1 = Gdx.input.isKeyJustPressed(Input.Keys.NUM_1);
        KEY_2 = Gdx.input.isKeyJustPressed(Input.Keys.NUM_2);
        KEY_3 = Gdx.input.isKeyJustPressed(Input.Keys.NUM_3);

    }

}
