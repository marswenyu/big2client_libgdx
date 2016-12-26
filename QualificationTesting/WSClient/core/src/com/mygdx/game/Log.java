package com.mygdx.game;

import java.util.LinkedList;

/**
 * Created by jay on 16/12/25.
 */
public class Log {

    public static void log(String content){
        System.out.println(NewGameManager.TAG+content);
    }

    public static void printCard(String content, LinkedList<OneCard> cardList){
        StringBuilder sb = new StringBuilder();

        if(cardList != null) {
            for (OneCard card : cardList) {
                sb.append(card.getValue() + ",");
            }

            sb.deleteCharAt(sb.length() - 1);
        }

        log(content + " : " +sb.toString());
    }
}
