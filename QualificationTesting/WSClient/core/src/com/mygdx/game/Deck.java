package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Deck {
    public ArrayList<Card> allCards = new ArrayList<Card>();

    //洗牌
    public ArrayList<Card> createAllCardsAndShuffle() {
        allCards.clear();

        PokerStyle[] styles = PokerStyle.values();
        for(PokerStyle style: styles){

            for(int i=1; i<=PokerStyle.MAXNUM; i++){
                String asset_name = String.format("pic_poker_%s_%d.png", style.getName(), i);

                String name = String.format("%s_%d", style.getName(), i);
                Texture img = new Texture("poker/" + asset_name);
                Sprite sprite = new Sprite(img);

                allCards.add(new Card(style.getRank(), i, name, 130, 200, sprite));
            }
        }

        Collections.shuffle(allCards);
        int i = 1;
        for(Card card:allCards){
            System.out.println(MyGdxGame.TAG+"card name:"+card.Name+" i="+i);
            i++;
        }

        return allCards;
    }

    //發牌
    public Player[] deal(Player[] players){
        System.out.println(MyGdxGame.TAG+"allCards size:"+allCards.size());


        int playCount = 0;
        for(Card card:allCards){

            if(players[playCount].playerCards.size() >= PokerStyle.MAXNUM){
                playCount++;
            }

            if(card.mPokeSuitRank == PokerStyle.Club.getRank() && card.mPokerRank == 3){
                NewGameManager.GlobalPlayerTurn = playCount; //拿到梅花三
            }

            players[playCount].playerCards.add(card);

        }

        for(Player player:players){

            Collections.sort(player.playerCards, comparator);

            float x =0;
            float y =0;
            float delta_x = 0;
            float delta_y = 0;
            float roation = 0;

            switch (player.mPlayerNameEnum){
                case Jhon:
                    x =300;
                    y =550;
                    delta_y = 25;
                    roation = 90;
                    break;
                case Mary:
                    x =350;
                    y =1020;
                    delta_x = 25;
                    break;
                case Tom:
                    x =1030;
                    y =550;
                    delta_y = 25;
                    roation = 90;
                    break;
                case Joe:
                    x =350;
                    y =310;
                    delta_x = 25;
                    break;
                default:
                    break;
            }

            for(int i=0; i<PokerStyle.MAXNUM; i++){
                player.playerCards.get(i).setPlayerAndPosition(x, y, roation);
                x+=delta_x;
                y+=delta_y;
            }
        }

        return  players;
    }

    Comparator<Card> comparator = new Comparator<Card>(){
        public int compare(Card s1, Card s2) {
            //先排年龄
            if(s1.mPokeSuitRank != s2.mPokeSuitRank){
                return s1.mPokeSuitRank - s2.mPokeSuitRank;
            }else {
                return s1.mPokerRank - s2.mPokerRank;
            }
        }
    };
}
