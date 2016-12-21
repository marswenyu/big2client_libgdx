package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import java.util.LinkedList;
import java.util.List;


public class NewGameManager {

    private List<Card> m_table_cards = new LinkedList<Card>();  //產生UI用的List
    private Player[] mAllPlayer;    //每個玩家以及手上的牌
    private boolean mIsGameFinish;
    Deck mDeck;

    Player[] players = {
            new Player(PlayerNameEnum.Jhon),
            new Player(PlayerNameEnum.Mary),
            new Player(PlayerNameEnum.Tom),
            new Player(PlayerNameEnum.Joe)
    };

    public void generate(){
        mIsGameFinish = false;
        mDeck = new Deck();
        mDeck.createAllCardsAndShuffle();
        mAllPlayer = mDeck.deal(players);
    }

    public void render(SpriteBatch batch){
        m_table_cards.clear();
        for(Player player:mAllPlayer){
            for(Card card:player.playerCards){
                m_table_cards.add(card);
            }
        }

        for(int i=0; i<m_table_cards.size(); i++){
            batch.draw(m_table_cards.get(i).Image,
                    m_table_cards.get(i).X, m_table_cards.get(i).Y,
                    0, 0,
                    m_table_cards.get(i).Width, m_table_cards.get(i).Height,
                    1, 1,
                    m_table_cards.get(i).Rotation);
        }

    }

    public void play(){
        for(Player player:mAllPlayer){
            if(player.playerCards.size() == 0){
                return; //有玩家出完牌,牌局結束
            }
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                    play();
            }
        }, 2000);
    }
}
