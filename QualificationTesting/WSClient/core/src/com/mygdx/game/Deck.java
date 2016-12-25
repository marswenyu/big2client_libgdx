package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Deck {

    //發牌
    public Player[] deal(Player[] players){

        ArrayList<OneCard> tmpCardList = new ArrayList<OneCard>();

        OneCard.Suit[] styles = OneCard.Suit.values();
        for(OneCard.Suit style: styles){

            for(int i=1; i<=OneCard.Suit.EACH_MAX_NUM; i++){

                tmpCardList.add(new OneCard(i, style));
            }
        }

//        Collections.shuffle(tmpCardList);

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
