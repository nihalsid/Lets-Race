package com.letsrace.game;

import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.network.FRGameClient;
import com.letsrace.game.network.FRGameServer;
import com.letsrace.game.network.FRGoogleServices;
import com.letsrace.game.screen.FRGameScreen;
import com.letsrace.game.screen.FRSplashScreen;
import com.letsrace.game.screen.FRWaitScreen;
import com.letsrace.game.screen.MenuScreen;
import com.letsrace.game.screen.MultiplayerMenuScreen;
import com.letsrace.game.screen.SplashScreen;

public class LetsRace extends Game {
	public GameState gameState;
	public BitmapFont font;
	public FRGoogleServices googleServices;
	public Stage stage;
	public Skin skin;
	public HashMap<String, Integer> playerNumber;
	public int myPlayerNo;
	public FRGameClient client;

	public LetsRace(FRGoogleServices services) {
		googleServices = services;
		playerNumber = new HashMap<String, Integer>();
	}
	
	public void decideOnServerAndStart() {
		String[] participants = (String[]) googleServices.getParticipantIds()
				.toArray();
		Arrays.sort(participants);
		if (participants[0] == googleServices.getMyId()) {
			Gdx.app.log(FRConstants.TAG, "I am the server");
			FRGameServer server = new FRGameServer(this, participants);
			googleServices.setServerListener(server);
		}
		int ctr = 0;
		for (String s : participants) {
			playerNumber.put(s, ctr++);
		}
		myPlayerNo = playerNumber.get(googleServices.getMyId());
		Gdx.app.log(FRConstants.TAG, "I am the client");
		client = new FRGameClient(participants[0]);
		googleServices.setClientListener(client);
		moveToScreen(GameState.CAR_SELECT);
	}

	@Override
	public void create() {
		font = new BitmapFont();
		skin = new Skin(
				new TextureAtlas(Gdx.files.internal("ui/ui-atlas.pack")));
		stage = new Stage();
		gameState = GameState.SPLASH;
		FRConstants.initializeDynamicConstants();
		Assets.load();
		setScreen(new SplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	public void dispose() {
		stage.dispose();
		font.dispose();
	}

	public void moveToScreen(GameState screen) {
		switch (screen) {
		case GAME_SCREEN:
			gameState = GameState.GAME_SCREEN;
			setScreen(new FRGameScreen(this));
			break;
		case MENU:
			gameState = GameState.MENU;
			setScreen(new MenuScreen(this));
			break;
		case WAIT:
			gameState = GameState.WAIT;
			setScreen(new FRWaitScreen(this));
			break;
		case SELECT_CAR:
			gameState = GameState.SELECT_CAR;
			break;
		case SPLASH_SIGN_IN:
			gameState = GameState.SPLASH_SIGN_IN;
			setScreen(new FRSplashScreen(this));
			break;
		case SPLASH:
			gameState = GameState.SPLASH;
			setScreen(new SplashScreen(this));
			break;
		case MULTIPLAYER_MENU:
			gameState = GameState.MULTIPLAYER_MENU;
			setScreen(new MultiplayerMenuScreen(this));
			break;
		default:
			break;
		}
	}
}
