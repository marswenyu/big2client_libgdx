package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import java.util.LinkedList;
import java.util.List;


public class NewGameManager {

    private List<Card> m_table_cards = new LinkedList<Card>();  //產生UI用的List
    private Player[] mHandPlayer;    //每個玩家手上的牌
    private Player[] mPoolPlayer;    //每個玩家出的牌
    private boolean mIsGameFinish;
    public static int GlobalPlayerTurn; //該誰出牌
    public static long GlobalPlayInterval = 2000L;
    public long tmpCurrent;
    Deck mDeck;

    Player[] players = {
            new Player(PlayerNameEnum.Jhon),
            new Player(PlayerNameEnum.Mary),
            new Player(PlayerNameEnum.Tom),
            new Player(PlayerNameEnum.Joe)
    };

    public void generate(){
        GlobalPlayerTurn = -1;
        tmpCurrent = 0L;
        mIsGameFinish = false;
        mDeck = new Deck();
        mDeck.createAllCardsAndShuffle();
        mHandPlayer = mDeck.deal(players);
    }

    public void render(SpriteBatch batch){
        long nowTime = System.currentTimeMillis();
        if(nowTime - tmpCurrent >= GlobalPlayInterval){
            // TODO: 觸發玩家出牌
            for(Player player:mHandPlayer){
                if(player.playerCards.size() > 0) {
                    player.playerCards.remove(0);
                }
            }
            tmpCurrent = nowTime;
        }

        m_table_cards.clear();
        for(Player player:mHandPlayer){
            for(Card card:player.playerCards){
                m_table_cards.add(card);
            }
        }

        if(mPoolPlayer != null) {
            for (Player player : mPoolPlayer) {
                for (Card card : player.playerCards) {
                    m_table_cards.add(card);
                }
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

}
