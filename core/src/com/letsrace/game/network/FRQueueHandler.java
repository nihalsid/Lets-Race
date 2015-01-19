package com.letsrace.game.network;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class FRQueueHandler {
	Queue<Message> queue;

	public FRQueueHandler() {
		queue = new LinkedBlockingQueue<Message>();
	}

	public void addToQueue(Message msg) {
		queue.add(msg);
	}

	public Message readQueue() {
		if (queue == null)
			return null;
		try {
			return queue.remove();
		} catch (NoSuchElementException exception) {
			return null;
		}
	}
}
