package com.letsrace.game.desktop;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.LetsRace;
import com.letsrace.game.network.FRGoogleServices;
import com.letsrace.game.network.FRMessageListener;
import com.letsrace.game.screen.FRMultiplayerMenuScreen;

public class DesktopLauncher implements FRGoogleServices {
	public LetsRace game;

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		DesktopLauncher launcher = new DesktopLauncher();
		launcher.game = new LetsRace(launcher);
		new LwjglApplication(launcher.game, config);
	}

	@Override
	public void initiateSignIn() {

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

	public ArrayList<String> getParticipantIds() {
		if (!game.multiplayer) {
			ArrayList<String> local = new ArrayList<String>();
			local.add("local");
			return local;
		} else {
			ArrayList<String> local = new ArrayList<String>();
			local.add("local");
			return local;
		}
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
		Task action = new Task() {
			@Override
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						if (game.gameState == GameState.MULTIPLAYER_MENU) {
							((FRMultiplayerMenuScreen) game.getScreen())
									.enableSignedInButtons();
						}
					}
				});
			}
		};
		Timer.schedule(action, 4);
		return false;
	}

	@Override
	public String getSinglePlayerIds() {
		// TODO Auto-generated method stub
		return null;
	}

}
