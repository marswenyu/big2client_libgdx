package com.big2.game.AI;

public class PlayerObjUnit {
    int cardNumber;
    int cardSuit;

    public PlayerObjUnit(int number, int suit) {
        cardNumber = number;
        cardSuit = suit;
    }
    
    public boolean isHigher(int number){
    	
    	return cardNumber > number;
    }
    
    public boolean isValueEqual(PlayerObjUnit objUnit){
    	return cardNumber == objUnit.cardNumber && cardSuit == objUnit.cardSuit;
    }
}
