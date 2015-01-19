package com.letsrace.game.network;

import static com.letsrace.game.network.FRMessageCodes.ACCEL_DOWN;
import static com.letsrace.game.network.FRMessageCodes.ACCEL_UP;
import static com.letsrace.game.network.FRMessageCodes.NO_ACCELERATE;
import static com.letsrace.game.network.FRMessageCodes.PING_DETECT_RES;
import static com.letsrace.game.network.FRMessageCodes.SELECTED_CAR_0;
import static com.letsrace.game.network.FRMessageCodes.SELECTED_CAR_1;
import static com.letsrace.game.network.FRMessageCodes.SELECTED_CAR_2;
import static com.letsrace.game.network.FRMessageCodes.SELECTED_CAR_3;
import static com.letsrace.game.network.FRMessageCodes.STEER_STRAIGHT;
import static com.letsrace.game.network.FRMessageCodes.TURN_LEFT;
import static com.letsrace.game.network.FRMessageCodes.TURN_RIGHT;

import java.nio.ByteBuffer;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRGameWorld;
import com.letsrace.game.LetsRace;
import com.letsrace.game.car.Car;
import com.letsrace.game.car.Car.Accel;
import com.letsrace.game.car.Car.Steer;

public class FRGameServer implements FRMessageListener, Runnable {
	public FRGameWorld gameWorld;
	LetsRace gameRef;
	HashMap<String, FRPlayerData> sessionData;
	FRQueueHandler queueHandler;
	boolean running = true;
	byte packetNumber = -128;

	public FRGameServer(LetsRace game, String[] participants) {
		Gdx.app.log(FRConstants.TAG, "FRGameServer(): Constructor");
		gameRef = game;
		gameWorld = new FRGameWorld();
		sessionData = new HashMap<String, FRPlayerData>();
		queueHandler = new FRQueueHandler();
		for (String s : participants)
			sessionData.put(s, new FRPlayerData());
	}

	private void relayMessage(byte m, String pid) {
		byte[] msg = new byte[1];
		msg[0] = FRMessageCodes.convertToCorrespondingPlayerMessageCode(m,
				gameRef.playerNumber.get(pid).intValue());
		gameRef.googleServices.broadcastReliableExceptPlayer(msg);
	}

	public void processMessage(byte[] buffer, String senderParticipantId){
		switch (buffer[0]) {
		case PING_DETECT_RES:
			sessionData.get(senderParticipantId).ping = (int) (System
					.currentTimeMillis() - timeInMillis) / 2;
			Gdx.app.log(FRConstants.TAG, "Ping(" + senderParticipantId + "):"
					+ sessionData.get(senderParticipantId).ping);
			break;
		case ACCEL_DOWN:
			gameWorld.carHandler.cars.get(gameRef.playerNumber.get(senderParticipantId)).accelerate = Accel.BRAKE;
			relayMessage(buffer[0], senderParticipantId);
			break;
		case ACCEL_UP:
			gameWorld.carHandler.cars.get(gameRef.playerNumber.get(senderParticipantId)).accelerate = Accel.ACCELERATE;
			relayMessage(buffer[0], senderParticipantId);
			break;
		case NO_ACCELERATE:
			gameWorld.carHandler.cars.get(gameRef.playerNumber.get(senderParticipantId)).accelerate = Accel.NONE;
			relayMessage(buffer[0], senderParticipantId);
			break;
		case TURN_LEFT:
			gameWorld.carHandler.cars.get(gameRef.playerNumber.get(senderParticipantId)).steer = Steer.LEFT;
			relayMessage(buffer[0], senderParticipantId);
			break;
		case TURN_RIGHT:
			gameWorld.carHandler.cars.get(gameRef.playerNumber.get(senderParticipantId)).steer = Steer.RIGHT;
			relayMessage(buffer[0], senderParticipantId);
			break;
		case STEER_STRAIGHT:
			gameWorld.carHandler.cars.get(gameRef.playerNumber.get(senderParticipantId)).steer = Steer.NONE;
			relayMessage(buffer[0], senderParticipantId);
			break;
		case SELECTED_CAR_0:
		case SELECTED_CAR_1:
		case SELECTED_CAR_2:
		case SELECTED_CAR_3:
			Gdx.app.log(FRConstants.TAG, "FRGameServer(): Car selection by: P"
					+ gameRef.playerNumber.get(senderParticipantId) + ", C:"
					+ FRMessageCodes.extractHeaderExtField(buffer[0]));
			checkCarAvailabilityAndReply(buffer[0], senderParticipantId);
			if (isAllPickConfirmed()) {
				measurePing();
				Timer.schedule(new Task() {
					@Override
					public void run() {
						byte[] msg = new byte[1];
						msg[0] = FRMessageCodes.PROCEED_TO_GAME_SCREEN;
						Gdx.app.log(FRConstants.TAG, "FRGameServer(): Proceed to gameScreen");
						gameRef.googleServices.broadcastReliableMessage(msg);
					}
				},1.5f);
				Timer.schedule(new Task() {
					@Override
					public void run() {
						FRGameServer.this.gameWorld.update(1f/FRConstants.SYNC_PACKETS_PER_SEC);
						FRGameServer.this.gameRef.googleServices.broadcastMessage(generateSyncPacket());
					}
				}, FRConstants.SYNC_PACKETS_START_DELAY, 1f/FRConstants.SYNC_PACKETS_PER_SEC);

			}
			break;
		}
	}

	@Override
	public void onMessageRecieved(byte[] buffer, String senderParticipantId) {
		Message msg = new Message(buffer, senderParticipantId);
		queueHandler.addToQueue(msg);
	}

	private void checkCarAvailabilityAndReply(byte msgHead,
			String senderParticipantId) {
		for (FRPlayerData d : sessionData.values()) {
			if (d.carCode == FRMessageCodes.extractHeaderExtField(msgHead)) {
				byte[] msg = new byte[1];
				msg[0] = FRMessageCodes.REPICK_CAR;
				gameRef.googleServices.sendReliableMessage(msg,
						senderParticipantId);
				return;
			}
		}
		sessionData.get(senderParticipantId).carCode = FRMessageCodes
				.extractHeaderExtField(msgHead);
		byte[] msg = new byte[2];
		msg[0] = (byte) (FRMessageCodes.CAR_PICK_CONFIRMED_PLAYER_0 | (byte) gameRef.playerNumber
				.get(senderParticipantId).intValue());
		msg[1] = FRMessageCodes.extractHeaderExtField(msgHead);
		gameRef.googleServices.broadcastReliableMessage(msg);
		gameWorld.carHandler.addCar(
				gameRef.playerNumber.get(senderParticipantId).intValue(),
				FRMessageCodes.extractHeaderExtField(msgHead));
	}

	private boolean isAllPickConfirmed() {
		for (FRPlayerData data : sessionData.values()) {
			if (data.carCode == Integer.MIN_VALUE)
				return false;
		}
		return true;
	}

	private long timeInMillis;

	public void measurePing() {
		byte[] msg = new byte[1];
		msg[0] = FRMessageCodes.PING_DETECT_REQ;
		timeInMillis = System.currentTimeMillis();
		gameRef.googleServices.broadcastReliableMessage(msg);
	}

	public byte[] generateSyncPacket() {
		int ctr = 0;
		byte[] message = new byte[1 + 1 + gameWorld.carHandler.cars.size() * 20];
		message[ctr++] = FRMessageCodes.RESYNC_HEAD;
		message[ctr++] = packetNumber++;
		for (int j = 0; j < gameWorld.carHandler.cars.size(); j++) {
			Car c = gameWorld.carHandler.cars.get(j);
			byte[] posX = ByteBuffer.allocate(4)
					.putFloat(c.getWorldPosition().x).array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = posX[i];
			byte[] posY = ByteBuffer.allocate(4)
					.putFloat(c.getWorldPosition().y).array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = posY[i];
			byte[] wAngle = ByteBuffer.allocate(4).putFloat(c.wheelAngle)
					.array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = wAngle[i];
			byte[] cBodyAngle = ByteBuffer.allocate(4)
					.putFloat(c.getBodyAngle()).array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = cBodyAngle[i];
			byte[] cSpeed = ByteBuffer.allocate(4).putFloat(c.getSpeedKMH())
					.array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = cSpeed[i];
		}
		return message;
	}

	@Override
	public void run() {
		Message msg;
		while (running) {
			if ((msg = queueHandler.readQueue()) != null) {
				processMessage(msg.msg, msg.id);
			}
		}
	}

}
