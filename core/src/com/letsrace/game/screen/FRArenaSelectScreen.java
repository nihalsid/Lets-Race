package com.letsrace.game.screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.letsrace.game.CameraTweenAccessor;
import com.letsrace.game.FRConstants;
import com.letsrace.game.LetsRace;
import com.letsrace.game.unused.FRAssets;

public class FRArenaSelectScreen extends ScreenAdapter implements
		GestureListener {

	private LetsRace gameRef;
	SpriteBatch batch;
	Camera camera;
	Vector3 touchPoint;
	ArrayList<String> arenaNames;
	int currentPosition = 1;

	Sprite logo;
	Sprite heading;
	Sprite trackOverview;
	Sprite arrow;
	Sprite background;
	Sprite endHint;

	TweenManager tweenMgr;
	GestureDetector gesture;

	float camHalfWidth;
	float camHalfHeight;

	private boolean isAnimating = false;
	private boolean showLeft = false, showRight = false;

	public FRArenaSelectScreen(LetsRace letsRace) {
		Gdx.app.log(FRConstants.TAG, "Menu: Constructor");
		gameRef = letsRace;
		batch = new SpriteBatch();
		camera = new OrthographicCamera(6, 10);
		camHalfWidth = camera.viewportWidth / 2f;
		camHalfHeight = camera.viewportHeight / 2f;
		camera.position.set(currentPosition * camHalfWidth, camHalfHeight, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		touchPoint = new Vector3();
		tweenMgr = new TweenManager();
		gesture = new GestureDetector(this);
		Gdx.input.setInputProcessor(gesture);
		Tween.registerAccessor(Camera.class, new CameraTweenAccessor());
		loadNames();
	}

	private void loadNames() {
		arenaNames = new ArrayList<String>();
		FileHandle handle = Gdx.files.internal("arenaNames.txt");
		if (handle.exists()) {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					handle.read()));
			String name;
			try {
				while ((name = input.readLine()) != null) {
					arenaNames.add(name);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void show() {
		Gdx.app.log(FRConstants.TAG, "Menu: Show()");
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tweenMgr.update(delta);
		batch.begin();
		endHint = FRAssets.arenaScreenAtlas.createSprite("endHint");
		renderTile(currentPosition);
		renderTile(currentPosition - 1);
		renderTile(currentPosition + 1);
		endHint.setSize(camHalfWidth * 2, camHalfHeight * 2);
		endHint.setPosition((currentPosition - 1) * camHalfWidth * 2, 0);
		if (showLeft) {
			endHint.draw(batch);
		}
		if (showRight) {
			endHint.flip(true, false);
			endHint.draw(batch);
		}
		batch.end();
	}

	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Arena: Dispose()");
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if (velocityX > 5f) {
			moveLeft();
		} else if (velocityX < -5f) {
			moveRight();
		}
		return false;
	}

	private void moveLeft() {
		Gdx.app.log(FRConstants.TAG, "Move Left !");
		if (currentPosition > 1) {
			currentPosition = currentPosition - 1;
			Tween.to(camera, CameraTweenAccessor.POSITION, 1.0f)
					.target((currentPosition - 1) * camHalfWidth * 2
							+ camHalfWidth, camHalfHeight)
					.setCallback(myCallBack)
					.setCallbackTriggers(TweenCallback.END).start(tweenMgr);
			isAnimating = true;
		} else {
			showLeft = true;
			Timer.schedule(new Task() {
				public void run() {
					showLeft = false;
				}
			}, 0.3f);
		}
	}

	private void moveRight() {
		Gdx.app.log(FRConstants.TAG, "Move Right !");
		if (currentPosition < arenaNames.size()) {
			currentPosition = currentPosition + 1;
			Tween.to(camera, CameraTweenAccessor.POSITION, 1.0f)
					.target((currentPosition - 1) * camHalfWidth * 2
							+ camHalfWidth, camHalfHeight)
					.setCallback(myCallBack)
					.setCallbackTriggers(TweenCallback.END).start(tweenMgr);
			isAnimating = true;
		} else {
			showRight = true;
			Timer.schedule(new Task() {
				public void run() {
					showRight = false;
				}
			}, 0.3f);
		}
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	private void renderTile(int pos) {
		if (pos < 1 || pos > arenaNames.size()) {
			return;
		} else {
			batch.setProjectionMatrix(camera.combined);
			String name = arenaNames.get(pos - 1);
			logo = FRAssets.arenaScreenAtlas.createSprite(name + "Logo");
			heading = FRAssets.arenaScreenAtlas.createSprite(name + "Heading");
			trackOverview = FRAssets.arenaScreenAtlas.createSprite(name
					+ "Track");

			float xOffset = (pos - 1) * camHalfWidth * 2;

			background = FRAssets.background;
			background.setSize(camHalfWidth * 2, camHalfHeight * 2);
			background.setPosition(xOffset, 0);

			arrow = FRAssets.arenaScreenAtlas.createSprite("nextArrow");

			heading.setSize(5, 1);
			heading.setPosition(0.5f + xOffset, 8);

			logo.setSize(5, 4);
			logo.setPosition(1 + xOffset, 3.5f);

			trackOverview.setSize(3.5f, 2.0f);
			trackOverview.setPosition(xOffset + 1.25f, 1.0f);

			background.draw(batch);
			heading.draw(batch);
			logo.draw(batch);
			trackOverview.draw(batch);

			if (!isAnimating) {
				arrow.setSize(0.5f, 1.2f);
				if (currentPosition > 1) {
					arrow.setPosition(xOffset + 0.2f, camHalfHeight);
					arrow.draw(batch);
				}
				if (currentPosition < arenaNames.size()) {
					arrow.flip(true, false);
					arrow.setPosition(xOffset + 0.2f + 5f, camHalfHeight);
					arrow.draw(batch);
				}
			}

		}
	}

	TweenCallback myCallBack = new TweenCallback() {
		public void onEvent(int type, BaseTween<?> source) {
			isAnimating = false;
		}
	};
}