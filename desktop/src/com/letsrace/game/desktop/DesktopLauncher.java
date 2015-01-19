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
import com.letsrace.game.network.Message;
import com.letsrace.game.screen.FRMultiplayerMenuScreen;

public class DesktopLauncher implements FRGoogleServices {
	public LetsRace game;
	FRMessageHandler messageHandler;
	
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

	}

	@Override
	public String getMyId() {
		return "MastBanda";
	}

	@Override
	public ArrayList<String> getParticipantIds() {
		ArrayList<String> p = new ArrayList<String>();
		p.add(getMyId());
		return p;
	}

	@Override
	public void setServerListener(FRMessageListener listener) {
		messageHandler.setServerMessageListener(listener);
	}

	@Override
	public void setClientListener(FRMessageListener listener) {
		messageHandler.setClientMessageListener(listener);
	}

	public void sendReliableMessage(byte[] message, String participantID) {
		triggerMessageRecieveWithConstantDelay(message);
	}

	public void broadcastMessage(byte[] message) {
		triggerMessageRecieveWithVariableDelay(message);
	}

	@Override
	public void broadcastReliableMessage(byte[] message) {
		triggerMessageRecieveWithConstantDelay(message);
	}
	
	public void broadcastReliableExceptPlayer(byte[] message){
		
	}
	private static final float FIXED_DELAY = 0.35f;
	public void triggerMessageRecieveWithVariableDelay(final byte[] message) {
		Timer.schedule(new Task() {
			@Override
			public void run() {
				messageHandler.onRealTimeMessageReceived(new Message(
						message,getMyId()));
			}
		}, (float)(FIXED_DELAY+0.05*Math.random()));
	}
	public void triggerMessageRecieveWithConstantDelay(final byte[] message) {
		Timer.schedule(new Task() {
			@Override
			public void run() {
				messageHandler.onRealTimeMessageReceived(new Message(
						message,getMyId()));
			}
		}, (float)(FIXED_DELAY));
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
	public void setupSinglePlayerVars() {
		messageHandler=new FRMessageHandler();
	}

}
