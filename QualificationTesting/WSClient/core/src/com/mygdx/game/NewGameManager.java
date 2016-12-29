package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedList;


public class NewGameManager {
    public static String TAG = "MyGdxGame_";

    private LinkedList<PositionAndImg.PositionDetail> drawCardList = new LinkedList<PositionAndImg.PositionDetail>();  //產生UI用的List
    private boolean mIsGameFinish;
    private boolean mIsRoundFinish;
    public static int GlobalPlayerTurn; //該誰出牌
    public static long GlobalPlayInterval = 2000L;
    public long tmpCurrent;
    Deck mDeck;
    private int mPlayTurn = -1;
    private boolean mReadyToPlay;

    public static Sprite passImage;

    private LinkedList<OneCard> mWithCard = null;  //玩家出的牌

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
        mIsRoundFinish =false;
        mDeck = new Deck();
        mDeck.deal(players);
        mReadyToPlay = true;

        passImage= new Sprite(new Texture("poker/pic_poker.png"));

    }

    public void render(SpriteBatch batch){
        startGame();

        drawCardList.clear();
        drawCardList = new PositionAndImg().createPosition(players);

        LinkedList<PositionAndImg.PositionDetail> toPlay = new PositionAndImg().createFirePosition(players);
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

    public void startGame(){
        long nowTime = System.currentTimeMillis();
        if(tmpCurrent == 0)
            tmpCurrent = nowTime;

        if((nowTime - tmpCurrent >= GlobalPlayInterval)) {
            playCard();
            tmpCurrent = nowTime;
        }
    }

    public void playCard(){

        long nowTime = System.currentTimeMillis();

        for(Player player:players){
            if(player.playerOneCards.size() == 0) {
                mIsGameFinish = true;
            }
        }

        if(checkResetStartNextRound()){
            return;
        }

        if((nowTime - tmpCurrent >= GlobalPlayInterval) && !mIsGameFinish){
            for(int i=0;i<players.length;i++){
                if(players[i].getIsYourTurn()){

                    players[i].playerToReturn =
                            players[i].caculateHowToPlay(players[i].playerOneCards, mWithCard== null ? null:mWithCard);


                    if(players[i].playerToReturn == null){
                        players[i].setPass();
                    }else {
                        players[i].playerOneCards.removeAll(players[i].playerToReturn);
                        mWithCard = players[i].playerToReturn;
                    }

                    players[i].resetYourTurn();

                    int nextPlayer = i+1==4 ? 0:i+1;

                    while (players[nextPlayer].getPass()){
                        nextPlayer++;
                        if(nextPlayer==4){
                            nextPlayer = 0;
                        }
                    }
                    players[nextPlayer].setYourTurn();

                    break;
                }
            }
        }
    }

    public boolean checkResetStartNextRound(){
        int passCount = 0;

        for(Player player:players) {
            if(player.getPass()){
                passCount++;
            }
        }
        if(passCount == 3){
            //這一round結束
            for(Player player:players) {
                player.resetPass();

                if(player.playerToReturn != null) {
                    player.playerToReturn.clear();
                }
            }

            if(mWithCard != null) {
                mWithCard = null;
            }

            return true;
        }

        return false;
    }
}
