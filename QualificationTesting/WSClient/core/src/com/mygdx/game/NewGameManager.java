package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import java.util.LinkedList;
import java.util.List;


public class NewGameManager {

    private LinkedList<OneCard> m_table_cards = new LinkedList<OneCard>();  //產生UI用的List
    private LinkedList<PositionAndImg.PositionDetail> drawCardList = new LinkedList<PositionAndImg.PositionDetail>();  //產生UI用的List
    private Player[] mHandPlayer;    //每個玩家手上的牌
    private Player[] mPoolPlayer;    //每個玩家出的牌
    private boolean mIsGameFinish;
    public static int GlobalPlayerTurn; //該誰出牌
    public static long GlobalPlayInterval = 2000L;
    public long tmpCurrent;
    Deck mDeck;
    private int mPlayTurn = -1;
    private boolean mReadyToPlay;

    private LinkedList<OneCard> mWithCard = new LinkedList<OneCard>();  //玩家出的牌

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
        mHandPlayer = mDeck.deal(players);
        mReadyToPlay = true;

    }

    public void render(SpriteBatch batch){
        long nowTime = System.currentTimeMillis();
        if((nowTime - tmpCurrent >= GlobalPlayInterval) && !mIsGameFinish && !mReadyToPlay){
            // TODO: 觸發玩家出牌

            LinkedList<OneCard> tmpWithCard = null;
            mWithCard.clear();

            int passCount = 0; //若出現三個Pass,代表

            //第一個人先出牌
            for(int i=0;i<mHandPlayer.length;i++){
                if(players[i].getIsYourTurn()){
                    mPlayTurn = i;
                    tmpWithCard = mHandPlayer[i].caculateHowToPlay(mHandPlayer[i].playerOneCards, null);
                    mWithCard = (LinkedList<OneCard>) tmpWithCard.clone();
                }
            }

            //第二個人開始出牌
            while (true && !mWithCard.isEmpty()){

                for(int i=mPlayTurn;i<mHandPlayer.length;i++){

                    if(!mHandPlayer[i].getPass()){
                        tmpWithCard = mHandPlayer[i].caculateHowToPlay(mHandPlayer[i].playerOneCards, mWithCard);

                        if(tmpWithCard != null){
                            mWithCard = (LinkedList<OneCard>) tmpWithCard.clone();
                        }else{
                            mHandPlayer[i].setPass();
                            passCount++;
                        }
                    }

                    if(mPlayTurn == 3){
                        mPlayTurn =0;
                    }else {
                        mPlayTurn++;
                    }
                }

                if(passCount == 3) {
                    break;
                }
            }

            for(Player player:mHandPlayer){
                if(player.playerOneCards.size() == 0) {
                    mIsGameFinish = true;
                }

                player.resetPass();
                player.resetYourTurn();
            }
            mWithCard.clear();
            mHandPlayer[mPlayTurn].setYourTurn();
            tmpCurrent = nowTime;
        }

        drawCardList = new PositionAndImg().createPosition(mHandPlayer);
        Player winner = new Player(PlayerNameEnum.getPlayerEnum(mPlayTurn));
        winner.playerOneCards = (LinkedList<OneCard>) mWithCard.clone();
        LinkedList<PositionAndImg.PositionDetail> toPlay = new PositionAndImg().createFirePosition(winner);
        drawCardList.addAll(toPlay);

        for(int i=0; i<drawCardList.size(); i++){
            batch.draw(drawCardList.get(i).Image,
                    drawCardList.get(i).X, drawCardList.get(i).Y,
                    0, 0,
                    drawCardList.get(i).Width, drawCardList.get(i).Height,
                    1, 1,
                    drawCardList.get(i).Rotation);
        }

    }

}
