package com.letsrace.game.network;

import java.util.ArrayList;

public interface FRGoogleServices {
	void initiateSignIn();
	void startQuickGame();
	public ArrayList<String> getParticipantIds();
	public String getMyId();
}
