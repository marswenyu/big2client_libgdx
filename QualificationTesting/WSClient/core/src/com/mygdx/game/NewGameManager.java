package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import java.util.LinkedList;
import java.util.List;


public class NewGameManager {
    public static String TAG = "MyGdxGame_";

    private LinkedList<OneCard> m_table_cards = new LinkedList<OneCard>();  //產生UI用的List
    private LinkedList<PositionAndImg.PositionDetail> drawCardList = new LinkedList<PositionAndImg.PositionDetail>();  //產生UI用的List
//    private Player[] mHandPlayer;    //每個玩家手上的牌
//    private Player[] mPoolPlayer;    //每個玩家出的牌
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
//        if((nowTime - tmpCurrent >= GlobalPlayInterval) && !mIsGameFinish && !mReadyToPlay){
//            // TODO: 觸發玩家出牌
//
//            LinkedList<OneCard> tmpWithCard = null;
//            mWithCard.clear();
//
//            int passCount = 0; //若出現三個Pass,代表
//
//            //第一個人先出牌
//            for(int i=0;i<mHandPlayer.length;i++){
//                if(players[i].getIsYourTurn()){
//                    mPlayTurn = i;
//                    tmpWithCard = mHandPlayer[i].caculateHowToPlay(mHandPlayer[i].playerOneCards, null);
//                    mWithCard = (LinkedList<OneCard>) tmpWithCard.clone();
//                }
//            }
//
//            //第二個人開始出牌
//            while (true && !mWithCard.isEmpty()){
//
//                for(int i=mPlayTurn;i<mHandPlayer.length;i++){
//
//                    if(!mHandPlayer[i].getPass()){
//                        tmpWithCard = mHandPlayer[i].caculateHowToPlay(mHandPlayer[i].playerOneCards, mWithCard);
//
//                        if(tmpWithCard != null){
//                            mWithCard = (LinkedList<OneCard>) tmpWithCard.clone();
//                        }else{
//                            mHandPlayer[i].setPass();
//                            passCount++;
//                        }
//                    }
//
//                    if(mPlayTurn == 3){
//                        mPlayTurn =0;
//                    }else {
//                        mPlayTurn++;
//                    }
//                }
//
//                if(passCount == 3) {
//                    break;
//                }
//            }
//
//            for(Player player:mHandPlayer){
//                if(player.playerOneCards.size() == 0) {
//                    mIsGameFinish = true;
//                }
//
//                player.resetPass();
//                player.resetYourTurn();
//            }
//            mWithCard.clear();
//            mHandPlayer[mPlayTurn].setYourTurn();
//            tmpCurrent = nowTime;
//        }

        drawCardList.clear();
        drawCardList = new PositionAndImg().createPosition(players);
//        Player winner = new Player(PlayerNameEnum.getPlayerEnum(mPlayTurn));
//        winner.playerOneCards = (LinkedList<OneCard>) mWithCard.clone();

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
//            Log.log("startGame()");
            playCard();
            tmpCurrent = nowTime;
        }
    }

    public void playCard(){
//        Log.log("playCard()");

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

                    Log.log(players[i].mPlayerNameEnum.getName()+" > caculateHowToPlay");

                    players[i].playerToReturn =
                            players[i].caculateHowToPlay(players[i].playerOneCards, mWithCard== null ? null:mWithCard);


                    if(players[i].playerToReturn == null){
                        Log.log(players[i].mPlayerNameEnum.getName()+" > pass");
                        players[i].setPass();
                    }else {
                        boolean isRemove = players[i].playerOneCards.removeAll(players[i].playerToReturn);

                        Log.printCard(players[i].playerToReturn);

                        mWithCard = players[i].playerToReturn;
                        Log.log(players[i].mPlayerNameEnum.getName()+" > no pass");
                    }

                    players[i].resetYourTurn();

                    int nextPlayer = i+1==4 ? 0:i+1;

                    while (players[nextPlayer].getPass()){
                        nextPlayer++;
                        if(nextPlayer==4){
                            nextPlayer = 0;
                        }
                    }

                    Log.log(players[i].mPlayerNameEnum.getName()+" > yourTurn:"+players[i].getIsYourTurn());
                    players[nextPlayer].setYourTurn();
                    Log.log(players[nextPlayer].mPlayerNameEnum.getName()+" > yourTurn:"+players[nextPlayer].getIsYourTurn());


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
