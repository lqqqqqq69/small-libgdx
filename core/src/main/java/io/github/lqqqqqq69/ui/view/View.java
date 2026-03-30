package io.github.lqqqqqq69.ui.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.lqqqqqq69.ui.model.ViewModel;

/**
 * View ist die Basisklasse fuer alle UI-Views im Spiel
 */
public abstract class View <T extends ViewModel> extends Table{
    protected final T viewModel;
    protected final Skin skin;
    protected final Stage stage;

    public View(T viewModel, Skin skin, Stage stage) {
        super(skin);
        this.viewModel = viewModel;
        this.skin = skin;
        this.stage = stage;

        setupUI();
    }

    /**
     * setupUI ist eine abstrakte Methode fuer den Grundaufbau des UIs
     */
    protected abstract void setupUI();

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null) {
            this.viewModel.clearPropertyChanges();
        } else{
            setupPropertyChanges();
        }
    }

    protected void setupPropertyChanges() {

    }

    /**
     * onClick fuegt einem Actor einen Click-Handler hinzu
     * - Fuegt Actor einen ClickListener hinzu 
     * - ClickListener registriert Event fuer einen Klick auf den Actor
     * 
     * @param actor Actor (z.B. Button)
     * @param consumer auszufuehrende Funktion
     */
    public static void onClick(Actor actor, OnEventConsumer consumer){
        actor.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                consumer.onEvent();
            }
        });
    } 

    /**
     * onEnter fuegt einem Actor einen Mausberuehrungs-Handler hinzu
     * - Fuegt Actor einen InputListener hinzu 
     * - ClickListener registriert Event fuer die Beruehrung des Actors
     * 
     * @param <T> Art des Actors
     * @param actor Actor (z.B. Button)
     * @param consumer auszufuehrende Funktion
     */
    public static <T extends Actor> void onEnter(T actor, OnActorEvent<T> consumer){
        actor.addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                consumer.onEvent(actor);
            }

        });
    } 

    /**
     * onChange fuegt einem Actor einen Change-Handler hinzu
     * - Fuegt Actor einen ChangeListener hinzu
     * - ChangeListener registriert Event fuer eine Zustandsaenderung des Actors
     * 
     * @param <T> Art des Actors
     * @param actor Actor (z.B. Slider)
     * @param consumer auszufuehrende Funktion
     */
    public static <T extends Actor> void onChange(T actor, OnActorEvent<T> consumer){
        actor.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor eventActor){
                consumer.onEvent(actor);
            }

        });
    } 


    
    @FunctionalInterface
    public interface OnEventConsumer{
        void onEvent();

    }

    @FunctionalInterface
    public interface OnActorEvent<T extends Actor>{
        void onEvent(T actor);
    }

}