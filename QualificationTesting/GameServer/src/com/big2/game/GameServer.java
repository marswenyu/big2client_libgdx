package com.big2.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class GameServer extends WebSocketServer {

	public GameServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public GameServer(InetSocketAddress address) {
		super(address);
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {

		sendToAll(conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ " 进入房间 ！");

		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ " connected");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {

		sendToAll(conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ " 离开房间 ！");

		System.out.println(conn.getRemoteSocketAddress().getAddress()
				.getHostAddress()
				+ " disconnected");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {

		sendToAll("["
				+ conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ "]" + message);

		System.out.println("["
				+ conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ "]" + message);
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
			String stringIn = webSocketIn.readLine();
			server.sendToAll(stringIn);
		}
	}
}
