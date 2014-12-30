package com.letsrace.game.network;

import com.letsrace.game.FRGameWorld;
import static com.letsrace.game.network.FRMessageCodes.*;

public class FRGameServer implements FRMessageListener {
	FRGameWorld gameWorld;
	
	public FRGameServer() {
		
	}

	@Override
	public void onMessageRecieved(byte[] buffer, String senderParticipantId) {
		switch (buffer[0]) {
		case ACCEL_DOWN:
			break;
		case ACCEL_UP:
			break;
		case TURN_LEFT:
			break;
		case TURN_RIGHT:
			break;
		case SELECTED_CAR_0:
			break;
		case SELECTED_CAR_1:
			break;
		case SELECTED_CAR_2:
			break;
		case SELECTED_CAR_3:
			break;
		case ACK_PICK_CONFIRMED:
			break;
		}
	}

}
