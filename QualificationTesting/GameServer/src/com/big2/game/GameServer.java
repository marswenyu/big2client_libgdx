package com.big2.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.big2.game.AI.GameAI;
import com.big2.game.AI.PlayerObj;
import com.big2.game.AI.PlayerObjUnit;
import com.google.gson.Gson;

public class GameServer extends WebSocketServer {

	public GameServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public GameServer(InetSocketAddress address) {
		super(address);
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		
		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ " connected");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {

		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ " disconnected");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {

		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ " message:"+ message);
		if(GameSwitch.GAME_SERVER_STATE.equals(message)){
			sendToAll(GameSwitch.GAME_SERVER_READY);
		}else{
			Gson gson = new Gson();
			PlayerObj player = gson.fromJson(message, PlayerObj.class);
			
			LinkedList<PlayerObjUnit> toBeat = player.getMyToBeatCard();
			if(toBeat == null || toBeat.size() < 1){
				toBeat = null;
			}
			
			LinkedList<PlayerObjUnit> toReturn = 
					GameAI.caculateHowToPlay(player.getMyHandCard(), toBeat);
			
			if(toReturn != null){
				player.getMyHandCard().removeAll(toReturn);
	
			}
			player.setMyReturnCard(toReturn);
			
			if(player.getMyHandCard().size() == 0){
				player.setmGameState(GameSwitch.GAME_DONE);
			}
			
			String gsonString = gson.toJson(player);
			System.out.println("Return to client message:"+ gsonString);
			sendToAll(gsonString);
		}
	}

	@Override
	public void onError(WebSocket conn, Exception e) {
		e.printStackTrace();
		if (conn != null) {
			conn.close();
		}
	}

	private void sendToAll(String text) {
		Collection<WebSocket> conns = connections();
		synchronized (conns) {
			for (WebSocket client : conns) {
				client.send(text);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {

		int port = 8887;

		GameServer server = new GameServer(port);
		server.start();

		System.out.println("Ready and Waiting Port::" + server.getPort());

		BufferedReader webSocketIn = new BufferedReader(new InputStreamReader(
				System.in));

		while (true) {
//			String stringIn = webSocketIn.readLine();
//			server.sendToAll(stringIn);
		}
	}
}
