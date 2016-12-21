package com.mygdx.game;


import java.util.LinkedList;
import java.util.List;

public class Player {

    public PlayerNameEnum mPlayerNameEnum;
    public List<Card> playerCards = new LinkedList<Card>();
    public boolean isContainClueThree;

    public Player(PlayerNameEnum playerNameEnum) {
        super();
        mPlayerNameEnum = playerNameEnum;
        isContainClueThree = false;
    }

    public void showCardPair(){

    }

    public void setIsContainClueThree(){
        isContainClueThree = true;
    }
}
