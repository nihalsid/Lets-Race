package com.letsrace.game.network;

public class FRSlidingWindow {
	Message messages[];
	boolean valid[];
	int size;
	int ticker;

	public FRSlidingWindow() {
		messages = new Message[256];
		valid = new boolean[messages.length];
		size = 127;
		ticker = 0;
		for (int i = 0; i < size; i++) {
			if (i < size)
				valid[i] = true;
			else
				valid[i] = false;
			messages[i] = null;
		}
	}

	public Message get() {
		Message retval = valid[ticker] ? messages[ticker] : null;
		valid[ticker] = false;
		valid[(ticker + size) % valid.length] = true;
		messages[ticker] = null;
		ticker = (ticker + 1) % valid.length;
		return retval;
	}
	
	public void put(Message m){
		if (valid[m.msg[1]+128])
			messages[m.msg[1]+128]=m;
	}
}
