package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Deck {

    //發牌
    public Player[] deal(Player[] players){

        LinkedList<OneCard> tmpCardList = new LinkedList<OneCard>();

        OneCard.Suit[] styles = OneCard.Suit.values();
        for(OneCard.Suit style: styles){

            for(int i=1; i<=OneCard.Suit.EACH_MAX_NUM; i++){

                tmpCardList.add(new OneCard(i, style));
            }
        }

        Collections.shuffle(tmpCardList);

        int playCount = 0;
        for(OneCard card:tmpCardList){

            if(players[playCount].playerOneCards.size() >= OneCard.Suit.EACH_MAX_NUM){
                playCount++;
            }


            players[playCount].playerOneCards.add(card);

        }

        return  players;
    }

}
