package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.letsrace.game.FRConstants;
import com.letsrace.game.LetsRace;
import com.letsrace.game.FRConstants.GameState;

public class FRSplashScreen extends ScreenAdapter {
	private LetsRace gameRef;
	float totalTime = 0;

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
//		Task action = new Task() {
//			@Override
//			public void run() {
//				gameRef.moveToScreen(GameState.MENU);
//			}
//		};
//		Timer.schedule(action, 3);
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		totalTime +=delta;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameRef.stage.draw();
		if(totalTime > 3f){
			gameRef.moveToScreen(GameState.MENU);
		}
	}

	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Splash: Dispose()");
	}

}
