package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.LetsRace;

public class SplashScreen extends ScreenAdapter {
	private LetsRace gameRef;
	Camera camera;
	Sprite fr;
	SpriteBatch spriteBatch;

	float totalTime;

	public SplashScreen(LetsRace game) {
		Gdx.app.log(FRConstants.TAG, "FRSplash: Constructor");
		gameRef = game;
		camera = new OrthographicCamera(6, 10);
		camera.position.set(camera.viewportWidth / 2f,
				camera.viewportHeight / 2f, 0);
		camera.update();
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);
		fr = FRAssets.frIcon;
		fr.setSize(1.5f, 1.0f);
		fr.setPosition(2.25f, 4.5f);
		totalTime = 0.0f;
	}

	public void show() {
		Gdx.app.log(FRConstants.TAG, "FRSplash: Show()");
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		totalTime += delta;
		if (totalTime > 3.0f) {
			gameRef.moveToScreen(GameState.MENU);
		} else {
			spriteBatch.begin();
			fr.draw(spriteBatch);
			spriteBatch.end();
		}
	}

	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Splash: Dispose()");
	}

}
