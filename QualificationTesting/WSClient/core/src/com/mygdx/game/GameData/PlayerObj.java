package com.mygdx.game.GameData;

import java.util.LinkedList;

/**
 * Created by jay on 16/12/31.
 */
public class PlayerObj {

    private String mGameState;
    private int playerOrder;
    private LinkedList<PlayerObjUnit> myHandCard;
    private LinkedList<PlayerObjUnit> myReturnCard;
    private LinkedList<PlayerObjUnit> myToBeatCard;

    public int getPlayerOrder() {
        return playerOrder;
    }

    public void setPlayerOrder(int playerOrder) {
        this.playerOrder = playerOrder;
    }

    public String getmGameState() {
        return mGameState;
    }

    public void setmGameState(String mGameState) {
        this.mGameState = mGameState;
    }

    public LinkedList<PlayerObjUnit> getMyToBeatCard() {
        return myToBeatCard;
    }

    public void setMyToBeatCard(LinkedList<PlayerObjUnit> myToBeatCard) {
        this.myToBeatCard = myToBeatCard;
    }

    public LinkedList<PlayerObjUnit> getMyHandCard() {
        return myHandCard;
    }

    public void setMyHandCard(LinkedList<PlayerObjUnit> myHandCard) {
        this.myHandCard = myHandCard;
    }

    public LinkedList<PlayerObjUnit> getMyReturnCard() {
        return myReturnCard;
    }

    public void setMyReturnCard(LinkedList<PlayerObjUnit> myReturnCard) {
        this.myReturnCard = myReturnCard;
    }
}
