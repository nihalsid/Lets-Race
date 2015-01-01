package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.LetsRace;
import com.letsrace.game.OverlapTester;

public class MenuScreen extends ScreenAdapter {

	private LetsRace gameRef;
	SpriteBatch batch;
	Sprite singleplayer;
	Sprite multiplayer;
	Sprite setting;
	Sprite background;

	Rectangle singleplayerButton;
	Rectangle multiplayerButton;
	Rectangle settingButton;

	Camera camera;
	Vector3 touchPoint;

	public MenuScreen(LetsRace letsRace) {
		Gdx.app.log(FRConstants.TAG, "Menu: Constructor");
		gameRef = letsRace;
		batch = new SpriteBatch();
		singleplayer = FRAssets.singleplayerButton;
		multiplayer = FRAssets.multiplayerButton;
		setting = FRAssets.settingsIcon;
		background = FRAssets.background;

		camera = new OrthographicCamera(6, 10);
		camera.position.set(camera.viewportWidth / 2f,
				camera.viewportHeight / 2f, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		singleplayer.setPosition(0, 2.5f);
		singleplayer.setSize(5, 1f);

		singleplayerButton = new Rectangle(0, 2.5f, 5, 1);

		multiplayer.setPosition(1, 1.3f);
		multiplayer.setSize(5, 1f);
		multiplayerButton = new Rectangle(1, 1.3f, 5, 1);

		setting.setPosition(0.2f, 0.2f);
		setting.setSize(0.75f, 0.75f);
		settingButton = new Rectangle(0.2f, 0.2f, 0.75f, 0.75f);

		background.setPosition(0, 0);
		background.setSize(6, 10);

		touchPoint = new Vector3();
	}

	public void show() {
		Gdx.app.log(FRConstants.TAG, "Menu: Show()");
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));

			if (OverlapTester.pointInRectangle(singleplayerButton,
					touchPoint.x, touchPoint.y)) {
				Gdx.app.log(FRConstants.TAG, "SinglePLayer Button Clicked");
				singlePlayerButtonClicked();
			}
			if (OverlapTester.pointInRectangle(multiplayerButton, touchPoint.x,
					touchPoint.y)) {
				Gdx.app.log(FRConstants.TAG, "Multiplayer Button Clicked");
				multiPlayerButtonClicked();
			}
			if (OverlapTester.pointInRectangle(settingButton, touchPoint.x,
					touchPoint.y)) {
				Gdx.app.log(FRConstants.TAG, "Settings Button Clicked");
				settingsButtonClicked();
			}
		}
		
		batch.begin();
		background.draw(batch);
		singleplayer.draw(batch);
		multiplayer.draw(batch);
		setting.draw(batch);
		batch.end();
	}

	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Menu: Dispose()");
	}

	private void singlePlayerButtonClicked() {

	}

	private void multiPlayerButtonClicked() {
		gameRef.moveToScreen(GameState.MULTIPLAYER_MENU);
	}

	private void settingsButtonClicked() {

	}

}