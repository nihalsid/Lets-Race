package com.letsrace.game.input;

import com.badlogic.gdx.Gdx;
import com.letsrace.game.car.Car.Steer;
import com.letsrace.game.network.FRGoogleServices;
import com.letsrace.game.network.FRMessageCodes;

public class FRNetworkInputHandler extends FRInputAdapter{
	public FRGoogleServices networkServices;
	public String serverID;
	private Steer lastMessage=Steer.NONE;
	
	public FRNetworkInputHandler(FRGoogleServices networkServices, String server){
		this.networkServices = networkServices;
		this.serverID = server;
	}
	
	public void handleAccelerometer(){
		final float NOISE = 1.5f;
		float x = Gdx.input.getAccelerometerX();
		if (x>=NOISE && lastMessage!=Steer.RIGHT){
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.TURN_RIGHT;
			networkServices.sendReliableMessage(msg, serverID);
			lastMessage=Steer.RIGHT;
		}else if(x<=-NOISE&lastMessage!=Steer.LEFT){
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.TURN_LEFT;
			networkServices.sendReliableMessage(msg, serverID);
			lastMessage=Steer.LEFT;
		}else if(lastMessage!=Steer.NONE){
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.STEER_STRAIGHT;
			networkServices.sendReliableMessage(msg, serverID);
			lastMessage=Steer.NONE;
		}
	}
	
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		byte[] msg = new byte[1];
		msg[0] = FRMessageCodes.ACCEL_UP;
		networkServices.sendReliableMessage(msg, serverID);
		return true;
	}

	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		byte[] msg = new byte[1];
		msg[0] = FRMessageCodes.NO_ACCELERATE;
		networkServices.sendReliableMessage(msg, serverID);
		return true;
	}

	
}
