package io.github.lqqqqqq69.ui.model;

import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.GameScreen;
import io.github.lqqqqqq69.Main;

/**
 * MenuViewModel enthaelt die von MenuView verwendeten Methoden fuer:
 * - Anpassung der Musiklautstaerke
 * - Anpassung der Soundlautstaerke
 * - Start eines Levels
 * - Schließen des Spiels
 */
public class MenuViewModel extends ViewModel {
    private final AudioService audioService;

    public MenuViewModel(Main game) {
        super(game);
        this.audioService = game.getAudioService();
    }
    
    public float getMusicVolume() {
        return audioService.getMusicVolume();
    }

    /**
     * setMusicVolume passt die Musiklautstaerke an
     * @param volume
     */
    public void setMusicVolume(float volume) {
        audioService.setMusicVolume(volume);
    }

    public float getSoundVolume() {
        return audioService.getSoundVolume();
    }

    /**
     * setSoundVolume passt die Soundlautstaerke an
     * 
     * @param volume gewuenschte Lautstaerke
     */
    public void setSoundVolume(float volume) {
        audioService.setSoundVolume(volume);
    }
    
    /**
     * startGame startet das ausgewaehlte Level (wechsel zu GameScreen)
     * 
     * @param level ausgewaehltes Level
     */
    public void startGame(int level) {
       game.setScreen(new GameScreen(game, level));
    }

    public void quitGame() {
        System.exit(0);
    }

    
}
