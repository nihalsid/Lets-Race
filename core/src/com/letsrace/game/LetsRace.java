package com.letsrace.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.letsrace.game.screen.FRGameScreen;

public class LetsRace extends Game {
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new FRGameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
