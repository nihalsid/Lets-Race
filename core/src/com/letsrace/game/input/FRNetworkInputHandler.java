package com.letsrace.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.letsrace.game.car.Car;
import com.letsrace.game.car.Car.Accel;
import com.letsrace.game.car.Car.Steer;
import com.letsrace.game.network.FRGoogleServices;
import com.letsrace.game.network.FRMessageCodes;

public class FRNetworkInputHandler extends FRInputAdapter{
	public FRGoogleServices networkServices;
	public String serverID;
	private Car myCar;
	
	public FRNetworkInputHandler(FRGoogleServices networkServices, String server, Car car){
		this.networkServices = networkServices;
		this.serverID = server;
		this.myCar = car;
	}
	
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		byte[] msg = new byte[1];
		if (screenX >Gdx.graphics.getWidth()*0.85&&screenY>Gdx.graphics.getHeight()*0.85){
			msg[0] = FRMessageCodes.ACCEL_DOWN;
		}
		else if (screenX<Gdx.graphics.getWidth()/2){
			msg[0] = FRMessageCodes.TURN_LEFT;
		}
		else if (screenX>Gdx.graphics.getWidth()/2){
			msg[0] = FRMessageCodes.TURN_RIGHT;
		}
		networkServices.sendReliableMessage(msg, serverID);
		return true;
	}

	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		byte[] msg1 = new byte[1];
		msg1[0] = FRMessageCodes.STEER_STRAIGHT;
		networkServices.sendReliableMessage(msg1, serverID);
		byte[] msg2 = new byte[1];
		msg2[0] = FRMessageCodes.ACCEL_UP;
		networkServices.sendReliableMessage(msg2, serverID);
		return true;
	}
	
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.DPAD_UP) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.ACCEL_UP;
			networkServices.sendReliableMessage(msg, serverID);
			myCar.accelerate = Accel.ACCELERATE;
			return true;
		} else if (keycode == Input.Keys.DPAD_DOWN) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.ACCEL_DOWN;
			networkServices.sendReliableMessage(msg, serverID);
			myCar.accelerate = Accel.BRAKE;
			return true;
		} else if (keycode == Input.Keys.DPAD_LEFT) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.TURN_LEFT;
			myCar.steer = Steer.LEFT;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		} else if (keycode == Input.Keys.DPAD_RIGHT) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.TURN_RIGHT;
			myCar.steer = Steer.RIGHT;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		}
		return false;
	}
	
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.DPAD_UP) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.NO_ACCELERATE;
			networkServices.sendReliableMessage(msg, serverID);
			myCar.accelerate = Accel.NONE;
			return true;
		} else if (keycode == Input.Keys.DPAD_DOWN) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.NO_ACCELERATE;
			networkServices.sendReliableMessage(msg, serverID);
			myCar.accelerate = Accel.NONE;
			return true;
		} else if (keycode == Input.Keys.DPAD_LEFT) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.STEER_STRAIGHT;
			myCar.steer = Steer.NONE;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		} else if (keycode == Input.Keys.DPAD_RIGHT) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.STEER_STRAIGHT;
			networkServices.sendReliableMessage(msg, serverID);
			myCar.steer = Steer.NONE;
			return true;
		}
		return false;
	}

	
}
