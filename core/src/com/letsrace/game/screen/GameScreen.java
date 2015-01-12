package com.letsrace.game.screen;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRGameRenderer;
import com.letsrace.game.FRGameWorld;
import com.letsrace.game.GameRenderer;
import com.letsrace.game.LetsRace;
import com.letsrace.game.Message;
import com.letsrace.game.car.Car;
import com.letsrace.game.input.FRInputAdapter;
import com.letsrace.game.input.GameInputAdapter;
import com.letsrace.game.network.FRMessageCodes;
import com.letsrace.game.network.ServerUtils;

public class GameScreen extends ScreenAdapter {
	public LetsRace gameRef;
	public FRGameRenderer renderer;
	public FRGameWorld gameWorldRef;
	public final boolean debug = true;
	public FRInputAdapter adapter;
	public boolean isServer;
	public GameInputAdapter input;
	public Car myCar;
	public GameRenderer gameRenderer;
	public SpriteBatch batch;

	enum ScreenState {
		RUNNING;
	}

	ScreenState screenState;

	ArrayList<String> participantIds;

	public GameScreen(LetsRace letsRace) {
		this.gameRef = letsRace;
		this.isServer = letsRace.isServer();
		this.screenState = ScreenState.RUNNING;
		this.gameRef.setUpCars();
		this.input = new GameInputAdapter(letsRace);
		this.participantIds = gameRef.googleServices.getParticipantIds();
		this.myCar = gameRef.clientWorld.players.get(gameRef.myId).car;
		this.batch = new SpriteBatch();
		this.gameRenderer = new GameRenderer(gameRef.clientWorld, batch, myCar);
		this.gameRef.clientWorld.mapHandler.setupContactListener(myCar.body);
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(float delta) {
		if (isServer) {
			updateServer(delta);
		}
		switch (screenState) {
		case RUNNING:
			updateRunning(delta);
			renderRunning(delta);
		}
	}

	private void renderRunning(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameRenderer.render();
	}

	private void updateRunning(float delta) {
		gameRef.clientWorld.physicalWorld.step(delta, 4, 2);
		gameRef.clientWorld.physicalWorld.clearForces();
		Message msg;
		while ((msg = gameRef.network.readFromClientQueue()) != null) {
			processServerMessage(msg);
		}
	}

	private void processServerMessage(Message msg) {
		byte[] message = msg.msg;
		switch (message[0]) {
		case FRMessageCodes.SYNC_CARS:
			processSyncCarsMessage(message);
			break;
		}
	}

	private void processSyncCarsMessage(byte[] packet) {
		for (int offset = 1; offset < packet.length; offset += 21) {
			int ctr = 0;
			String participantId = participantIds.get(packet[offset + ctr++]);
			Car c = gameRef.clientWorld.players.get(participantId).car;
			byte[] m = new byte[4];
			for (int i = 0; i < 4; i++) {
				m[i] = packet[offset + ctr++];
			}
			float posX = ByteBuffer.wrap(m).getFloat();
//			Gdx.app.log(FRConstants.TAG, posX+"PosX");
			for (int i = 0; i < 4; i++) {
				m[i] = packet[offset + ctr++];
			}
			float posY = ByteBuffer.wrap(m).getFloat();
//			Gdx.app.log(FRConstants.TAG, posY+"PosY");
			for (int i = 0; i < 4; i++) {
				m[i] = packet[offset + ctr++];
			}
			float wAngle = ByteBuffer.wrap(m).getFloat();
			c.wheelAngle = wAngle;
			for (int i = 0; i < 4; i++) {
				m[i] = packet[offset + ctr++];
			}
			float cBodyAngle = ByteBuffer.wrap(m).getFloat();
			c.setTransform(posX, posY, cBodyAngle);
			for (int i = 0; i < 4; i++) {
				m[i] = packet[offset + ctr++];
			}
			float cSpeed = ByteBuffer.wrap(m).getFloat();
			c.setSpeed(cSpeed);
		}
	}

	private void updateServer(float delta) {
		Message msg;
		while ((msg = gameRef.network.readFromServerQueue()) != null) {
			processClientMessage(msg);
		}
		ServerUtils.updateCars(gameRef.serverWorld, delta);
		gameRef.serverWorld.physicalWorld.step(delta, 4, 2);
		gameRef.serverWorld.physicalWorld.clearForces();
		byte[] carSyncMessage = ServerUtils.generateCarSyncMessage(
				gameRef.serverWorld, participantIds);
		gameRef.network.broadcastMessage(new Message(carSyncMessage, ""));
	}

	private void processClientMessage(Message message) {
		byte buf[] = message.msg;
		String senderId = message.id;
		switch (buf[0]) {
		case FRMessageCodes.MOVE_LEFT:
			gameRef.serverWorld.players.get(senderId).car.steer = Car.Steer.LEFT;
			break;
		case FRMessageCodes.MOVE_RIGHT:
			gameRef.serverWorld.players.get(senderId).car.steer = Car.Steer.RIGHT;
			break;
		case FRMessageCodes.ACCELERATE:
			gameRef.serverWorld.players.get(senderId).car.accelerate = Car.Accel.ACCELERATE;
			break;
		case FRMessageCodes.BRAKE:
			gameRef.serverWorld.players.get(senderId).car.accelerate = Car.Accel.BRAKE;
			break;
		case FRMessageCodes.STEER_NONE:
			gameRef.serverWorld.players.get(senderId).car.steer = Car.Steer.NONE;
			break;
		case FRMessageCodes.ACCELERATE_NONE:
			gameRef.serverWorld.players.get(senderId).car.accelerate = Car.Accel.NONE;
			break;
		}
	}

}
