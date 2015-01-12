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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.LetsRace;
import com.letsrace.game.Message;
import com.letsrace.game.network.FRMessageCodes;
import com.letsrace.game.network.ServerUtils;
import com.letsrace.game.unused.FRAssets;

public class CarSelectScreen extends ScreenAdapter implements GestureListener {

	class Stat {
		int P;
		int A;
		int S;
	}

	LetsRace gameRef;
	SpriteBatch batch;
	Camera camera;
	Vector3 touchPoint;
	ArrayList<String> characterNames;
	ArrayList<Stat> characterStats;
	int currentPosition = 1;

	Sprite logo;
	Sprite heading;

	Sprite arrow;
	Sprite background;
	Sprite endHint;
	Sprite power;
	Sprite acceleration;
	Sprite steering;

	TweenManager tweenMgr;
	GestureDetector gesture;

	float camHalfWidth;
	float camHalfHeight;

	private boolean isAnimating = false;
	private boolean showLeft = false, showRight = false;

	boolean isServer;

	BitmapFont font;

	ArrayList<Integer> selectedCarNumbers;

	public CarSelectScreen(LetsRace letsRace) {
		Gdx.app.log(FRConstants.TAG, "ArenaSelect: Constructor");
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
		font = new BitmapFont();
		loadNames();
		selectedCarNumbers = new ArrayList<Integer>();
		isServer = gameRef.isServer();
	}

	private void loadNames() {
		characterNames = new ArrayList<String>();
		characterStats = new ArrayList<CarSelectScreen.Stat>();
		FileHandle handle = Gdx.files.internal("characterNames.txt");
		if (handle.exists()) {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					handle.read()));
			String name;
			try {
				while ((name = input.readLine()) != null) {
					characterNames.add(name);
					FileHandle statsHandle = Gdx.files.internal(name
							+ "Stats.txt");
					String stat;
					Stat statObj = new Stat();
					BufferedReader statInput = new BufferedReader(
							new InputStreamReader(statsHandle.read()));

					while ((stat = statInput.readLine()) != null) {
						if (stat.charAt(0) == 'P') {
							statObj.P = Integer.parseInt(stat.substring(2));
						} else if (stat.charAt(0) == 'A') {
							statObj.A = Integer.parseInt(stat.substring(2));
						} else if (stat.charAt(0) == 'S') {
							statObj.S = Integer.parseInt(stat.substring(2));
						}
					}
					characterStats.add(statObj);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (isServer) {
			updateServer(delta);
		}

		updateScreen(delta);
		renderScreen(delta);

	}

	private void renderScreen(float delta) {
		tweenMgr.update(delta);
		batch.begin();
		endHint = FRAssets.carScreenAtlas.createSprite("endHint");
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

	private void updateScreen(float delta) {
		Message msg;
		while ((msg = gameRef.network.readFromClientQueue()) != null) {
			if (msg.msg[0] == FRMessageCodes.CAR_SELECTED) {
				selectedCarNumbers.add((int) msg.msg[1]);
			}
			if (msg.msg[0] == FRMessageCodes.CAR_SELECTION_CONFIRMED) {
				gameRef.clientWorld.players.get(gameRef.myId).playerType = msg.msg[1];
				gameRef.moveToScreen(GameState.GAME_SCREEN);
			}
		}
	}

	private void updateServer(float delta) {
		Message msg;
		while ((msg = gameRef.network.readFromServerQueue()) != null) {
			if (msg.msg[0] == FRMessageCodes.SELECT_CAR) {
				Gdx.app.log(FRConstants.TAG, "Car Select Message Received");
				int carNumber = msg.msg[1];
				if (ServerUtils.isCarSelected(gameRef.serverWorld, carNumber)) {
					byte[] message = new byte[1];
					message[0] = FRMessageCodes.CAR_SELECTED;
					gameRef.network.broadcastMessage(new Message(message,
							msg.id));
				} else {
					gameRef.serverWorld.players.get(msg.id).playerType = carNumber;
					byte[] message = new byte[3];
					message[0] = FRMessageCodes.CAR_SELECTION_CONFIRMED;
					message[1] = (byte) carNumber;
					gameRef.network.sendToClient(new Message(message, msg.id));
				}
			}
		}
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		byte message[] = new byte[2];
		message[0] = FRMessageCodes.SELECT_CAR;
		message[1] = (byte) currentPosition;
		gameRef.network.sendToServer(new Message(message, gameRef.serverId));
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
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
		if (currentPosition < characterNames.size()) {
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
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	private void renderTile(int pos) {
		if (pos < 1 || pos > characterNames.size()) {
			return;
		} else {
			batch.setProjectionMatrix(camera.combined);
			String name = characterNames.get(pos - 1);
			Stat statObj = characterStats.get(pos - 1);

			logo = FRAssets.carScreenAtlas.createSprite(name + "Logo");
			heading = FRAssets.carScreenAtlas.createSprite(name + "Title");

			float xOffset = (pos - 1) * camHalfWidth * 2;

			background = FRAssets.background;
			background.setSize(camHalfWidth * 2, camHalfHeight * 2);
			background.setPosition(xOffset, 0);

			arrow = FRAssets.carScreenAtlas.createSprite("nextArrow");

			heading.setSize(5, 1);
			heading.setPosition(0.5f + xOffset, 8);

			logo.setSize(4, 4);
			logo.setPosition(1 + xOffset, 3.5f);

			if (selectedCarNumbers.contains(pos)) {
				heading.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 0.60f);
				logo.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 0.60f);
			}
			
			background.draw(batch);
			heading.draw(batch);
			logo.draw(batch);

			if (!isAnimating) {
				arrow.setSize(0.5f, 1.2f);
				if (currentPosition > 1) {
					arrow.setPosition(xOffset + 0.2f, camHalfHeight);
					arrow.draw(batch);
				}
				if (currentPosition < characterNames.size()) {
					arrow.flip(true, false);
					arrow.setPosition(xOffset + 0.2f + 5f, camHalfHeight);
					arrow.draw(batch);
				}
			}

			Sprite power = FRAssets.powerBar;
			Sprite steeringbar = FRAssets.steeringBar;
			Sprite accnBar = FRAssets.accnBar;

			accnBar.setSize(statObj.A * 0.75f, 0.3f);
			accnBar.setPosition(xOffset + 1.5f, 0.9f);

			power.setSize(statObj.P * 0.75f, 0.3f);
			power.setPosition(xOffset + 1.5f, 1.3f);

			steeringbar.setSize(statObj.S * 0.75f, 0.3f);
			steeringbar.setPosition(xOffset + 1.5f, 1.7f);

			power.draw(batch);
			steeringbar.draw(batch);
			accnBar.draw(batch);

		}
	}

	TweenCallback myCallBack = new TweenCallback() {
		public void onEvent(int type, BaseTween<?> source) {
			isAnimating = false;
		}
	};
}