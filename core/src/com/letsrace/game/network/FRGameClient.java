package com.letsrace.game.network;


import static com.letsrace.game.network.FRMessageCodes.ACCEL_DOWN_PLAYER_0;
import static com.letsrace.game.network.FRMessageCodes.ACCEL_DOWN_PLAYER_1;
import static com.letsrace.game.network.FRMessageCodes.ACCEL_DOWN_PLAYER_2;
import static com.letsrace.game.network.FRMessageCodes.ACCEL_DOWN_PLAYER_3;
import static com.letsrace.game.network.FRMessageCodes.ACCEL_UP_PLAYER_0;
import static com.letsrace.game.network.FRMessageCodes.ACCEL_UP_PLAYER_1;
import static com.letsrace.game.network.FRMessageCodes.ACCEL_UP_PLAYER_2;
import static com.letsrace.game.network.FRMessageCodes.ACCEL_UP_PLAYER_3;
import static com.letsrace.game.network.FRMessageCodes.CAR_PICK_CONFIRMED_PLAYER_0;
import static com.letsrace.game.network.FRMessageCodes.CAR_PICK_CONFIRMED_PLAYER_1;
import static com.letsrace.game.network.FRMessageCodes.CAR_PICK_CONFIRMED_PLAYER_2;
import static com.letsrace.game.network.FRMessageCodes.CAR_PICK_CONFIRMED_PLAYER_3;
import static com.letsrace.game.network.FRMessageCodes.PING_DETECT_REQ;
import static com.letsrace.game.network.FRMessageCodes.PING_DETECT_RES;
import static com.letsrace.game.network.FRMessageCodes.PROCEED_TO_GAME_SCREEN;
import static com.letsrace.game.network.FRMessageCodes.REPICK_CAR;
import static com.letsrace.game.network.FRMessageCodes.RESYNC_HEAD;
import static com.letsrace.game.network.FRMessageCodes.TURN_LEFT_PLAYER_0;
import static com.letsrace.game.network.FRMessageCodes.TURN_LEFT_PLAYER_1;
import static com.letsrace.game.network.FRMessageCodes.TURN_LEFT_PLAYER_2;
import static com.letsrace.game.network.FRMessageCodes.TURN_LEFT_PLAYER_3;
import static com.letsrace.game.network.FRMessageCodes.TURN_RIGHT_PLAYER_0;
import static com.letsrace.game.network.FRMessageCodes.TURN_RIGHT_PLAYER_1;
import static com.letsrace.game.network.FRMessageCodes.TURN_RIGHT_PLAYER_2;
import static com.letsrace.game.network.FRMessageCodes.TURN_RIGHT_PLAYER_3;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRGameWorld;
import com.letsrace.game.LetsRace;
import com.letsrace.game.car.Car;
public class FRGameClient implements FRMessageListener{
	public FRGameWorld gameWorld;
	public LetsRace gameRef;
	public String serverID;
	
	public FRGameClient(LetsRace game, String serverID) {
		Gdx.app.log(FRConstants.TAG, "FRGameClient(): Constructor");
		this.serverID = serverID;
		this.gameRef = game;
		gameWorld = new FRGameWorld();
	}
	
	@Override
	public void onMessageRecieved(byte[] buffer, String participantId) {
		switch(buffer[0]){
		case PING_DETECT_REQ:
			byte[] msg = new byte[1];
			msg[0] = PING_DETECT_RES;
			gameRef.googleServices.sendReliableMessage(msg, serverID);
			break;
		case ACCEL_DOWN_PLAYER_0:
			break;
		case ACCEL_DOWN_PLAYER_1:
			break;
		case ACCEL_DOWN_PLAYER_2:
			break;
		case ACCEL_DOWN_PLAYER_3:
			break;
		case ACCEL_UP_PLAYER_0:
			break;
		case ACCEL_UP_PLAYER_1:
			break;
		case ACCEL_UP_PLAYER_2:
			break;
		case ACCEL_UP_PLAYER_3:
			break;
		case CAR_PICK_CONFIRMED_PLAYER_0:
		case CAR_PICK_CONFIRMED_PLAYER_1:
		case CAR_PICK_CONFIRMED_PLAYER_2:
		case CAR_PICK_CONFIRMED_PLAYER_3:
			int playerNumber = FRMessageCodes.extractHeaderExtField(buffer[0]);
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - CarPickConfirmed: P"+playerNumber+", C:"+buffer[1]);
			gameWorld.carHandler.addCar(playerNumber, buffer[1]);
			if (playerNumber == gameRef.myPlayerNo){
				Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - My car selection confirmed");
				gameWorld.mapHandler.setupContactListener(gameWorld.carHandler.cars.get(playerNumber).body);
			}
			break;
		case PROCEED_TO_GAME_SCREEN:
			Gdx.app.log(FRConstants.TAG, "FRGameClient(): MessageRecieved - ProceedToGameScreen");
			break;
		case REPICK_CAR:
			break;
		case RESYNC_HEAD:
			handleSyncPacket(buffer);
			break;
		case TURN_LEFT_PLAYER_0:
			break;
		case TURN_LEFT_PLAYER_1:
			break;
		case TURN_LEFT_PLAYER_2:
			break;
		case TURN_LEFT_PLAYER_3:
			break;
		case TURN_RIGHT_PLAYER_0:
			break;
		case TURN_RIGHT_PLAYER_1:
			break;
		case TURN_RIGHT_PLAYER_2:
			break;
		case TURN_RIGHT_PLAYER_3:
			break;
		}
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
		}
	}
}
