package com.letsrace.game.network;

import java.util.ArrayList;
import com.google.android.gms.games.multiplayer.Participant;

public interface FRGoogleServices {
	void initiateSignIn();
	void startQuickGame();
	public ArrayList<Participant> getParticipants();
	public String getMyId();
}
