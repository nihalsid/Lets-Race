package com.letsrace.game.network;

import java.util.ArrayList;

public interface FRGoogleServices {
	void initiateSignIn();
	void startQuickGame();
	public ArrayList<String> getParticipantIds();
	public String getMyId();
	public void setServerListener(FRMessageListener listener);
	public void setClientListener(FRMessageListener listener);
	public void sendReliableMessage(byte[] message, String participantID);
	public void broadcastMessage(byte[] message);
	public void broadcastReliableMessage(byte[] message);
	public boolean isSignedIn();
	public String getSinglePlayerIds();
	
}	
