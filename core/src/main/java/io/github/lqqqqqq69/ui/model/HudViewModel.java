package io.github.lqqqqqq69.ui.model;


import io.github.lqqqqqq69.GameScreen;
import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.MainMenuScreen;

/**
 * HudViewModel enthält die von HudView verwendeten Methoden fuer:
 * - Neustart eines Levels 
 * - Rueckkehr zum Menue
 */
public class HudViewModel extends ViewModel {

    public HudViewModel(Main game) {
        super(game);
    }

    /**
     * returnMenu fuehrt eine Rueckkehr zum Menue durch
     */
    public void returnMenu() {
        game.getScreen().dispose();
        game.setScreen(new MainMenuScreen(game));
    }

    /**
     * restartLevel startet ein Level neu
     * 
     * @param level aktuelles Level, welches nochmal gespielt werden soll
     */
    public void restartLevel(int level){
        game.getScreen().dispose();
        game.setScreen(new GameScreen(game, level));
    }

}