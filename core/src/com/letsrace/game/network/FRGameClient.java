package com.letsrace.game.network;


import com.badlogic.gdx.Gdx;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRGameWorld;
import com.letsrace.game.LetsRace;

import static com.letsrace.game.network.FRMessageCodes.*;

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
}
