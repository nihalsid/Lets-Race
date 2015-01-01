package com.letsrace.game.desktop;


import java.util.ArrayList;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.LetsRace;
import com.letsrace.game.network.FRGoogleServices;
import com.letsrace.game.network.FRMessageListener;

public class DesktopLauncher implements FRGoogleServices{
	public LetsRace game;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height =800;
		DesktopLauncher launcher = new DesktopLauncher();
		launcher.game = new LetsRace(launcher);
		new LwjglApplication(launcher.game, config);
	}

	@Override
	public void initiateSignIn() {
		Task action = new Task() {
			@Override
			public void run() {
				game.moveToScreen(GameState.MENU);
			}
		};
		Timer.schedule(action, 2);
	}

	@Override
	public void startQuickGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMyId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getParticipantIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setServerListener(FRMessageListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientListener(FRMessageListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendReliableMessage(byte[] message, String participantID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void broadcastMessage(byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void broadcastReliableMessage(byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSignedIn() {
		// TODO Auto-generated method stub
		return false;
	}
}
