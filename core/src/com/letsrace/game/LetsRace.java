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
import com.letsrace.game.network.FRGoogleServices;
import com.letsrace.game.screen.FRGameScreen;
import com.letsrace.game.screen.FRMenuScreen;
import com.letsrace.game.screen.FRSplashScreen;
import com.letsrace.game.screen.FRWaitScreen;

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
		String[] participants = (String[]) googleServices.getParticipantIds().toArray();
		Arrays.sort(participants);
		if (participants[0] == googleServices.getMyId())
			Gdx.app.log(FRConstants.TAG, "I am the server");
		int ctr = 0;
		for (String s : participants) {
			playerNumber.put(s, ctr++);
		}
		myPlayerNo = playerNumber.get(googleServices.getMyId());
		Gdx.app.log(FRConstants.TAG, "I am not the server");
	}

	@Override
	public void create() {
		font = new BitmapFont();
		skin = new Skin(new TextureAtlas(Gdx.files.internal("ui/ui-atlas.pack")));
		stage = new Stage();
		gameState = GameState.SPLASH_SIGN_IN;
		setScreen(new FRSplashScreen(this));
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
			setScreen(new FRMenuScreen(this));
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
		default:
			break;

		}
	}
}
