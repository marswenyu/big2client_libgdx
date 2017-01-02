package com.mygdx.game;

import java.util.LinkedList;
import java.util.List;

public class Player {
    public PlayerNameEnum mPlayerNameEnum;
    public LinkedList<OneCard> playerOneCards = new LinkedList<OneCard>();
    public LinkedList<OneCard> playerToReturn = new LinkedList<OneCard>();
    private boolean isYourTurn;
    private boolean isPass;

    public Player(PlayerNameEnum playerNameEnum) {
        super();
        mPlayerNameEnum = playerNameEnum;
        resetYourTurn();
        resetPass();
    }

    public boolean getIsYourTurn(){
        if(isContainClubsThree(playerOneCards)){
            isYourTurn = true;
        }

        return isYourTurn;
    }

    public void setYourTurn(){
        isYourTurn = true;
    }

    public void resetYourTurn(){
        isYourTurn = false;
    }

    public boolean getPass(){
        return isPass;
    }

    public void setPass(){
        isPass = true;
    }

    public void resetPass(){
        isPass = false;
    }

    public boolean isContainClubsThree(List<OneCard> hand){

        for(OneCard card:hand){
            if(GameAI.isClubsThree(card)){
                return true;
            }
        }

        return false;
    }
}
