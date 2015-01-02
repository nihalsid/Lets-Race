package com.letsrace.game.network;

public interface FRMessageListener {
	public void onMessageRecieved(byte[] buffer, String participantId);
}
