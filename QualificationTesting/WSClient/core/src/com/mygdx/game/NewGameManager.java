package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;


public class NewGameManager{
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
    private WebSocketClient mWebSocketClient;
    public static Sprite passImage;
    MyTextInputListener imputListener = new MyTextInputListener();

    private LinkedList<OneCard> mWithCard = null;  //玩家出的牌

    Player[] players = {
            new Player(PlayerNameEnum.Jhon),
            new Player(PlayerNameEnum.Mary),
            new Player(PlayerNameEnum.Tom),
            new Player(PlayerNameEnum.Joe)
    };

    private void initConfig(){
        GlobalPlayerTurn = -1;
        tmpCurrent = 0L;
        mIsGameFinish = false;
        mIsRoundFinish =false;
        mDeck = new Deck();
        mDeck.deal(players);
        mReadyToPlay = true;

        passImage= new Sprite(new Texture("poker/pic_poker.png"));
    }

    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
            connectToServer();
        }

        @Override
        public void canceled () {
            readyToConnect();
        }
    }

    public void readyToConnect(){

        Gdx.input.getTextInput(imputListener, "Connect Game Server", "ws://192.168.0.180:8887", "Enter Address");

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

    private void connectToServer(){
        try {
            mWebSocketClient = new WebSocketClient(new URI("ws://192.168.0.180:8887")) {
                @Override
                public void onMessage(String message) {
                    Log.log("===============onMessage============");
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.log("===============onOpen============");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.log("===============onClose============");
                }

                @Override
                public void onError(Exception ex) {
                    Log.log("===============onError============");
                    ex.printStackTrace();
                    readyToConnect();
                }

            };

            mWebSocketClient.connect();

        } catch (URISyntaxException ex) {
            Log.log("===============onError============");
        }
    }
}
