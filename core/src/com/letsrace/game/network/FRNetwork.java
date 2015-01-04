package com.letsrace.game.network;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.letsrace.game.Message;

public class FRNetwork {
	Queue<Message> serverQueue;
	Queue<Message> clientQueue;
	FRGoogleServices gservice;
	String serverId;
	String myId;

	public FRNetwork(FRGoogleServices gservice) {
		this.gservice = gservice;
		serverQueue = new LinkedBlockingQueue<Message>();
		clientQueue = new LinkedBlockingQueue<Message>();
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void setMyId(String myId) {
		this.myId = myId;
	}

	public void addToServerQueue(Message msg) {
		serverQueue.add(msg);
	}

	public void addToClientQueue(Message msg) {
		clientQueue.add(msg);
	}

	public Message readServerQueue() {
		if (serverQueue == null)
			return null;
		try {
			return serverQueue.remove();
		} catch (NoSuchElementException exception) {
			return null;
		}
	}

	public Message readClientQueue() {
		if (clientQueue == null)
			return null;
		try {
			return clientQueue.remove();
		} catch (NoSuchElementException exception) {
			return null;
		}
	}

	public void sendMessageToServer(Message msg) {
		if (serverId.equals(myId)) {
			Timer.schedule(new EnqueServerTask(msg),0.1f);
		} else {
			gservice.sendReliableMessage(msg.msg, serverId);
		}
	}
	
	public void sendMessageToClient(Message msg,String clientId){
		if(serverId.equals(clientId)){
			Timer.schedule(new EnqueClientTask(msg),0.1f);
		}else{
			gservice.sendReliableMessage(msg.msg, clientId);
		}
	}

	private class EnqueServerTask extends Task {
		Message msg;

		public EnqueServerTask(Message msg) {
			this.msg = msg;
		}

		public void run() {
			serverQueue.add(msg);
		}
	}

	private class EnqueClientTask extends Task {
		Message msg;

		public EnqueClientTask(Message msg) {
			this.msg = msg;
		}

		public void run() {
			clientQueue.add(msg);
		}
	}

}
