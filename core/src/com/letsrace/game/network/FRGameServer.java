package com.letsrace.game.network;

import static com.letsrace.game.network.FRMessageCodes.ACCEL_DOWN;
import static com.letsrace.game.network.FRMessageCodes.ACCEL_UP;
import static com.letsrace.game.network.FRMessageCodes.SELECTED_CAR_0;
import static com.letsrace.game.network.FRMessageCodes.SELECTED_CAR_1;
import static com.letsrace.game.network.FRMessageCodes.SELECTED_CAR_2;
import static com.letsrace.game.network.FRMessageCodes.SELECTED_CAR_3;
import static com.letsrace.game.network.FRMessageCodes.TURN_LEFT;
import static com.letsrace.game.network.FRMessageCodes.TURN_RIGHT;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRGameWorld;
import com.letsrace.game.LetsRace;

public class FRGameServer implements FRMessageListener {
	FRGameWorld gameWorld;
	LetsRace gameRef;
	public FRGameServer(LetsRace game, String[] participants) {
		Gdx.app.log(FRConstants.TAG, "FRGameServer(): Constructor");
		gameRef = game;
		gameWorld = new FRGameWorld();
		sessionData = new HashMap<String, FRPlayerData>();
		for (String s : participants)
			sessionData.put(s, new FRPlayerData());
	}
	HashMap<String, FRPlayerData> sessionData;
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
		case SELECTED_CAR_1:
		case SELECTED_CAR_2:
		case SELECTED_CAR_3:
			Gdx.app.log(FRConstants.TAG, "FRGameServer(): Car selection by: P"+gameRef.playerNumber.get(senderParticipantId)+", C:"+FRMessageCodes.extractHeaderExtField(buffer[0]));
			checkCarAvailabilityAndReply(buffer[0], senderParticipantId);
			if(isAllPickConfirmed()){
				byte msg[] = new byte[1];
				msg[0] = FRMessageCodes.PROCEED_TO_GAME_SCREEN;
				gameRef.googleServices.broadcastReliableMessage(msg);
			}
			break;
		}
	}
	
	private void checkCarAvailabilityAndReply(byte msgHead, String senderParticipantId){
		for (FRPlayerData d: sessionData.values()){
			if (d.carCode == FRMessageCodes.extractHeaderExtField(msgHead)){
				byte[] msg = new byte[1];
				msg[0] = FRMessageCodes.REPICK_CAR;
				gameRef.googleServices.sendReliableMessage(msg, senderParticipantId);
				return;
			}
		}
		sessionData.get(senderParticipantId).carCode = FRMessageCodes.extractHeaderExtField(msgHead);
		byte[] msg = new byte[2];
		msg[0] = (byte) (FRMessageCodes.CAR_PICK_CONFIRMED_PLAYER_0|(byte)gameRef.playerNumber.get(senderParticipantId).intValue());
		msg[1] = FRMessageCodes.extractHeaderExtField(msgHead);
		gameRef.googleServices.broadcastReliableMessage(msg);
		gameWorld.carHandler.addCar(gameRef.playerNumber.get(senderParticipantId).intValue(),  FRMessageCodes.extractHeaderExtField(msgHead));
	}
	
	private boolean isAllPickConfirmed(){
		for (FRPlayerData data: sessionData.values()){
			if (data.carCode == -1)
				return false;
		}
		return true;
	}
	

}
