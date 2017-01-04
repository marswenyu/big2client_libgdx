package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.google.gson.Gson;
import com.mygdx.game.GameData.PlayerObj;
import com.mygdx.game.GameData.TranformToPlayerObj;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;


public class NewGameManager{
    public static String TAG = "MyGdxGame_";

    private LinkedList<PositionAndImg.PositionDetail> drawCardList = new LinkedList<PositionAndImg.PositionDetail>();  //產生UI用的List
    private boolean mIsGameFinish;
    public static int GlobalPlayerTurn; //該誰出牌
    public static long GlobalPlayInterval = 2000L;
    public long tmpCurrent;
    Deck mDeck;
    private WebSocketClient mWebSocketClient;
    public static Sprite passImage;
    public static Sprite mDesk;
    MyTextInputListener imputListener = new MyTextInputListener();
    private boolean isFirstPlay;

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

        passImage= new Sprite(new Texture("poker/pic_poker.png"));
        mDesk= new Sprite(new Texture("poker/desk.png"));
    }

    public void dispose () {
        if(mWebSocketClient != null && mWebSocketClient.getReadyState() != WebSocket.READYSTATE.CLOSED){
            mWebSocketClient.close();
        }
    }

    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
            connectToServer(text);
        }

        @Override
        public void canceled () {
            Gdx.app.exit();
        }
    }

    public void readyToConnect(){
        Gdx.input.getTextInput(imputListener, "Connect Game Server", "ws://192.168.0.180:8887", "Enter Address");
    }

    private void connectToServer(String text){
        try {
            mWebSocketClient = new WebSocketClient(new URI(text)) {
                @Override
                public void onMessage(String message) {
                    Log.log("onMessage");
                    if(GameSwitch.GAME_SERVER_READY.equals(message)){
                        Log.log("onMessage:GAME_SERVER_READY");
                        //牌局初始化
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                initConfig();

                                mDeck = new Deck();
                                mDeck.deal(players);
                                startGameToServer();
                            }
                        });
                    }else {
                        Log.log("onMessage:"+message);
                        handleServerGameMessage(message);
                    }
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.log("===============onOpen============");

                    mWebSocketClient.send(GameSwitch.GAME_SERVER_STATE);

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.log("===============onClose============");
                }

                @Override
                public void onError(Exception ex) {
                    Log.log("===============onError============"+ex);
                    ex.printStackTrace();
                    readyToConnect();
                }

            };

            mWebSocketClient.connect();

        } catch (URISyntaxException ex) {
            Log.log("===============onError============");
        }
    }

    public void render(SpriteBatch batch){
//        startGame();
        if(mDesk != null){
            batch.draw(mDesk,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

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
                            GameAI.caculateHowToPlay(players[i].playerOneCards, mWithCard== null ? null:mWithCard);

                    Log.log("===============jsonString============");


                    Gson gson = new Gson();
                    String jsonString = gson.toJson(TranformToPlayerObj.handleSendToServer(i, players[i], null));
                    Log.log("===============jsonString============"+jsonString);

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

    private boolean isNeedResetStartNextRound(){
        int passCount = 0;

        for(Player player:players) {
            if(player.getPass()){
                passCount++;
            }
        }

        if(passCount == 3){
            return true;
        }

        return false;
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


    private void startGameToServer(){
        String jsonString = null;
        for(int i=0;i<players.length;i++){
            if(players[i].getIsYourTurn()){
                Gson gson = new Gson();
                jsonString = gson.toJson(TranformToPlayerObj.handleSendToServer(i, players[i], null));
                isFirstPlay = true;

                sendToServer(jsonString, i);
                break;
            }
        }
    }

    private void handleServerGameMessage(String message){
        if(message == null)
            return;



        Gson gson = new Gson();
        PlayerObj playerObj = gson.fromJson(message, PlayerObj.class);
        int playerTurn = playerObj.getPlayerOrder();

        if(GameSwitch.GAME_GOING.equals(playerObj.getmGameState()) || GameSwitch.GAME_DONE.equals(playerObj.getmGameState())){
            players[playerTurn].playerToReturn.clear();


            if(playerObj.getMyReturnCard() == null || playerObj.getMyReturnCard().size() < 1){
                Log.log("handleServerGameMessage:1");
                //玩家pass
                players[playerTurn].setPass();
            }else {
                Log.log("handleServerGameMessage:2");
                //玩家出牌
                TranformToPlayerObj.handleSendToClient(playerObj, players[playerTurn]);
                mWithCard = players[playerTurn].playerToReturn;
            }

            players[playerTurn].resetYourTurn();

            Log.log("handleServerGameMessage:3");


            int nextPlayer = playerTurn+1==4 ? 0:playerTurn+1;

            while (players[nextPlayer].getPass()){
                nextPlayer++;
                if(nextPlayer==4){
                    nextPlayer = 0;
                }
            }
            players[nextPlayer].setYourTurn();

            if(GameSwitch.GAME_GOING.equals(playerObj.getmGameState())){

                if(isNeedResetStartNextRound()) {

                    final int final_nextPlayer = nextPlayer;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            checkResetStartNextRound();


                            Gson tmpGson = new Gson();
                            String tmpString = tmpGson.toJson(TranformToPlayerObj.handleSendToServer(final_nextPlayer, players[final_nextPlayer], mWithCard== null ? null:mWithCard));

                            sendToServer(tmpString, final_nextPlayer);

                        }
                    }, 2);

                    return;
                }

                Log.log("handleServerGameMessage:4");
                Gson tmpGson = new Gson();
                String tmpString = tmpGson.toJson(TranformToPlayerObj.handleSendToServer(nextPlayer, players[nextPlayer], mWithCard== null ? null:mWithCard));

                sendToServer(tmpString, nextPlayer);
            }
        }
    }

    private void sendToServer(String message, final int turn){
        if(message == null)
            return;

        final String tmpMsg = message;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Log.log("player:"+String.valueOf(turn)+"  sendToServer:"+tmpMsg);
                        Log.log("=============");
                        mWebSocketClient.send(tmpMsg);
                    }
                });
            }
        }, 2);
    }
}
