package com.letsrace.game.network;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.letsrace.game.LetsRace;
import com.letsrace.game.Message;
import com.letsrace.game.Player;

public class FRNetwork {
	Queue<Message> serverQueue;
	Queue<Message> clientQueue;

	LetsRace gameRef;

	public static float LOCAL_DELAY = 0.4f;

	public FRNetwork(LetsRace gameRef) {
		this.gameRef = gameRef;
		serverQueue = new LinkedBlockingQueue<Message>();
		clientQueue = new LinkedBlockingQueue<Message>();
	}

	public void sendToServer(Message msg) {
		if (gameRef.isServer()) {
			Timer.schedule(new AddToServerQueue(msg), LOCAL_DELAY);
		} else {
			gameRef.googleServices.sendReliableMessage(msg.msg, gameRef.serverId);
		}
	}

	public void sendToClient(Message msg) {
		if (gameRef.isServer()) {
			Timer.schedule(new AddToClientQueue(msg), LOCAL_DELAY);
		} else {
			gameRef.googleServices.sendReliableMessage(msg.msg, msg.id);
		}
	}

	public void addToServerQueue(Message msg) {
		serverQueue.add(msg);
	}

	public void addToClientQueue(Message msg) {
		clientQueue.add(msg);
	}

	public Message readFromServerQueue() {
		if (serverQueue == null) {
			return null;
		}
		try {
			return serverQueue.poll();
		} catch (NoSuchElementException exp) {
			return null;
		}
	}

	public Message readFromClientQueue() {
		if (clientQueue == null) {
			return null;
		}
		try {
			return clientQueue.poll();
		} catch (NoSuchElementException exp) {
			return null;
		}
	}

	public void broadcastMessage(Message msg) {
		Set keySet = gameRef.serverWorld.players.keySet();
		for (Object key : keySet) {
			msg.id = (String) key;
			sendToClient(msg);
		}
	}

	private class AddToClientQueue extends Task {

		Message msg;

		public AddToClientQueue(Message msg) {
			this.msg = msg;
		}

		public void run() {
			clientQueue.add(msg);
		}
	}

	private class AddToServerQueue extends Task {

		Message msg;

		public AddToServerQueue(Message msg) {
			this.msg = msg;
		}

		public void run() {
			serverQueue.add(msg);
		}
	}

}
