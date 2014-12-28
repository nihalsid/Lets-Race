package com.letsrace.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.network.FRGoogleServices;
import com.letsrace.game.screen.FRGameScreen;
import com.letsrace.game.screen.FRMenuScreen;
import com.letsrace.game.screen.FRSplashScreen;
import com.letsrace.game.screen.FRWaitScreen;

public class LetsRace extends Game {
	public GameState gameState;
	public SpriteBatch batch;
	public BitmapFont font;
	public FRGoogleServices googleServices;
	public TextureAtlas textureAtlas;

	public LetsRace(FRGoogleServices services) {
		googleServices = services;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		textureAtlas = new TextureAtlas(Gdx.files.internal("ui/ui-atlas.pack"));
		gameState = GameState.SPLASH_SIGN_IN;
		setScreen(new FRSplashScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
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
