package io.github.lqqqqqq69.System;

/**
 * CashSystem setzt ein "Starguthaben" für jedes Level und updated das aktuelle Guthaben
 */
public class CashSystem{

    private int cashAmount; // aktuelles Guthaben

    /**
     * Der Konstruktor setzt das Startguthaben anhand des Levels
     * 
     * @param level aktuelles Level
     */
    public CashSystem(int level){
        if (level == 1){
            cashAmount = 80;
        }
        if (level == 2){
            cashAmount = 100;
        }
        if (level == 3){
            cashAmount = 150;
        }
    }

    public int getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(int cashAmount) {
        this.cashAmount = cashAmount;
    }

    


    

}
