package com.letsrace.game.network;

import com.letsrace.game.FRGameWorld;
import static com.letsrace.game.network.FRMessageCodes.*;

public class FRGameClient implements Runnable,FRMessageListener{
	public FRGameWorld gameWorld;

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
		case CAR_PICK_CONFIRMED:
			break;
		case PROCEED_TO_GAME_SCREEN:
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
