package com.mygdx.game;

import java.util.LinkedList;

/**
 * Created by jay on 16/12/25.
 */
public class Log {

    public static void log(String content){
        System.out.println(NewGameManager.TAG+content);
    }

    public static void printCard(LinkedList<OneCard> cardList){
        for(OneCard card:cardList){
            log(card.getOneCardName());
        }
    }
}
