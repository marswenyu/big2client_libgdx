package com.mygdx.game;


import java.util.ArrayList;
import java.util.List;

public class Player {

    public PlayerNameEnum mPlayerNameEnum;
    public List<Card> playerCards = new ArrayList<Card>();
    private boolean isFirstPlayCard;

    public Player(PlayerNameEnum playerNameEnum) {
        super();
        mPlayerNameEnum = playerNameEnum;
        isFirstPlayCard = false;
    }

}
