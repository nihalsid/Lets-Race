package com.letsrace.game.network;


import static com.letsrace.game.network.FRMessageCodes.*;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.FRGameWorld;
import com.letsrace.game.LetsRace;
import com.letsrace.game.Message;
import com.letsrace.game.car.Car;
import com.letsrace.game.car.Car.Accel;
import com.letsrace.game.car.Car.Steer;

public class FRGameClient implements FRMessageListener{
	public FRGameWorld gameWorld;
	public LetsRace gameRef;
	public String serverID;
	public FRQueueHandler queueHandler;
	
	public FRGameClient(LetsRace game, String serverID) {
		Gdx.app.log(FRConstants.TAG, "FRGameClient(): Constructor");
		this.serverID = serverID;
		this.gameRef = game;
		queueHandler = new FRQueueHandler();
		gameWorld = new FRGameWorld();
	}
	
	public void processMessage(byte[] buffer, String participantId){
		int playerNo;
		switch(buffer[0]){
		case PING_DETECT_REQ:
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - PingDetectReq");
			byte[] msg = new byte[1];
			msg[0] = PING_DETECT_RES;
			gameRef.googleServices.sendReliableMessage(msg, serverID);
			break;
		case ACCEL_DOWN_PLAYER_0:
		case ACCEL_DOWN_PLAYER_1:
		case ACCEL_DOWN_PLAYER_2:
		case ACCEL_DOWN_PLAYER_3:
			playerNo= FRMessageCodes.extractHeaderExtField(buffer[0]);
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - AccelDown: P"+playerNo);
			gameWorld.carHandler.cars.get(playerNo).accelerate = Accel.BRAKE;
			break;
		case ACCEL_UP_PLAYER_0:
		case ACCEL_UP_PLAYER_1:
		case ACCEL_UP_PLAYER_2:
		case ACCEL_UP_PLAYER_3:
			playerNo = FRMessageCodes.extractHeaderExtField(buffer[0]);
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - AccelUp: P"+playerNo);
			gameWorld.carHandler.cars.get(playerNo).accelerate = Accel.ACCELERATE;
			break;
		case NO_ACCELERATE_PLAYER_0:
		case NO_ACCELERATE_PLAYER_1:
		case NO_ACCELERATE_PLAYER_2:
		case NO_ACCELERATE_PLAYER_3:
			playerNo = FRMessageCodes.extractHeaderExtField(buffer[0]);
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - AccelNone: P"+playerNo);
			gameWorld.carHandler.cars.get(playerNo).accelerate = Accel.NONE;
			break;
			
		case CAR_PICK_CONFIRMED_PLAYER_0:
		case CAR_PICK_CONFIRMED_PLAYER_1:
		case CAR_PICK_CONFIRMED_PLAYER_2:
		case CAR_PICK_CONFIRMED_PLAYER_3:
			playerNo = FRMessageCodes.extractHeaderExtField(buffer[0]);
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - CarPickConfirmed: P"+playerNo+", C:"+buffer[1]);
			gameWorld.carHandler.addCar(playerNo, buffer[1]);
			if (playerNo == gameRef.myPlayerNo){
				Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - My car selection confirmed");
				gameWorld.mapHandler.setupContactListener(gameWorld.carHandler.cars.get(playerNo).body);
			}
			break;
		case PROCEED_TO_GAME_SCREEN:
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - ProceedToGameScreen");
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					gameRef.moveToScreen(GameState.GAME_SCREEN);
				}
			});
			break;
		case REPICK_CAR:
			break;
		case RESYNC_HEAD:
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - Resync");
			handleSyncPacket(buffer);
			break;
		case TURN_LEFT_PLAYER_0:
		case TURN_LEFT_PLAYER_1:
		case TURN_LEFT_PLAYER_2:
		case TURN_LEFT_PLAYER_3:
			playerNo = FRMessageCodes.extractHeaderExtField(buffer[0]);
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - TurnLeft: P"+playerNo);
			gameWorld.carHandler.cars.get(playerNo).steer = Steer.LEFT;
			break;
		case TURN_RIGHT_PLAYER_0:
		case TURN_RIGHT_PLAYER_1:
		case TURN_RIGHT_PLAYER_2:
		case TURN_RIGHT_PLAYER_3:
			playerNo = FRMessageCodes.extractHeaderExtField(buffer[0]);
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved -TurnRight: P"+playerNo);
			gameWorld.carHandler.cars.get(playerNo).steer = Steer.RIGHT;
			break;
		case STEER_STRAIGHT_PLAYER_0:
		case STEER_STRAIGHT_PLAYER_1:
		case STEER_STRAIGHT_PLAYER_2:
		case STEER_STRAIGHT_PLAYER_3:
			playerNo = FRMessageCodes.extractHeaderExtField(buffer[0]);
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - Steer Straight: P"+playerNo);
			gameWorld.carHandler.cars.get(playerNo).steer = Steer.NONE;
			break;
		}
	}
	
	@Override
	public void onMessageRecieved(byte[] buffer, String senderParticipantId) {
		Message msg = new Message(buffer, senderParticipantId);
		queueHandler.addToQueue(msg);
	}

	
	public void handleSyncPacket(byte[] packet){
		int ctr=1;
		for (int j=0;j<gameWorld.carHandler.cars.size();j++){
			Car c = gameWorld.carHandler.cars.get(j);
			byte[] m = new byte[4];
			for (int i=0;i<4;i++){
				m[i]=packet[ctr++];
			}
			float posX=ByteBuffer.wrap(m).getFloat();
			for (int i=0;i<4;i++){
				m[i]=packet[ctr++];
			}
			float posY=ByteBuffer.wrap(m).getFloat();
			for (int i=0;i<4;i++){
				m[i]=packet[ctr++];
			}
			float wAngle=ByteBuffer.wrap(m).getFloat();
			c.wheelAngle = wAngle;
			for (int i=0;i<4;i++){
				m[i]=packet[ctr++];
			}
			float cBodyAngle=ByteBuffer.wrap(m).getFloat();
			c.setTransform(posX, posY, cBodyAngle);
			for (int i=0;i<4;i++){
				m[i]=packet[ctr++];
			}
			float cSpeed=ByteBuffer.wrap(m).getFloat();
			c.setSpeed(cSpeed);
		}
	}
}
