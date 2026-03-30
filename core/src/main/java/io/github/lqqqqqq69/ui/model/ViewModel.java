package io.github.lqqqqqq69.ui.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import io.github.lqqqqqq69.Main;

/**
 * ViewModel ist die Basisklasse fuer alle ViewModels
 */
public class ViewModel {
    protected final Main game;
    protected final PropertyChangeSupport propertyChangeSupport;

    public ViewModel(Main game) {
        this.game = game;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * onPropertyChange registriert einen Listener, der auf die aenderung einer benannten Property reagiert
     * @param <T> Typ des neuen Property-Werts
     * @param propertyName Name der Property
     * @param propType Klasse des Typs T
     * @param consumer Funktion die bei Aenderung ausgefuehrt wird
     */
    public <T> void onPropertyChange(String propertyName, Class<T> propType, OnPropertyChange<T> consumer) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyName, evt -> {
            consumer.onChange(propType.cast(evt.getNewValue()));
        });
    }
    /**
     * clearPropertyChanges entfernt alle registrierten PropertyChangeListener
     */
    public void clearPropertyChanges() {
        for (PropertyChangeListener listener : this.propertyChangeSupport.getPropertyChangeListeners()) {
            this.propertyChangeSupport.removePropertyChangeListener(listener);
        }
    }
        

    @FunctionalInterface
    public interface OnPropertyChange<T> {
        /**
         * bei aenderung der beobachteten Property aufgerufen
         * @param value neuer Wert der Property
         */
        void onChange(T value);
    }

}
