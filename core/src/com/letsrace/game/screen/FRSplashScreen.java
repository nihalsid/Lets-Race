package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.LetsRace;

public class FRSplashScreen extends ScreenAdapter {
	private LetsRace gameRef;

	public FRSplashScreen(LetsRace game) {
		Gdx.app.log(FRConstants.TAG, "FRSplash: Constructor");
		gameRef = game;
		Gdx.input.setInputProcessor(gameRef.stage);
		Image image = new Image(gameRef.skin.getDrawable("fr."));
		image.setSize(image.getWidth()*FRConstants.GUI_SCALE_WIDTH,image.getHeight()*FRConstants.GUI_SCALE_WIDTH);
		image.setPosition((Gdx.graphics.getWidth()-image.getWidth())/2, (Gdx.graphics.getHeight()-image.getHeight())/2);
		gameRef.stage.addActor(image);
	}

	public void show() {
		Gdx.app.log(FRConstants.TAG, "FRSplash: Show()");
		Timer.schedule(new Task() {
			@Override
			public void run() {
				gameRef.moveToScreen(GameState.MENU);
			}
		}, 3);
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameRef.stage.draw();
	}

	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Splash: Dispose()");
	}

}
