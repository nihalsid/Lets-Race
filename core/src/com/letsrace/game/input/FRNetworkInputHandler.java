package com.letsrace.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.letsrace.game.network.FRGoogleServices;
import com.letsrace.game.network.FRMessageCodes;

public class FRNetworkInputHandler extends InputAdapter{
	public FRGoogleServices networkServices;
	public String serverID;
	
	public FRNetworkInputHandler(FRGoogleServices networkServices, String server){
		this.networkServices = networkServices;
		this.serverID = server;
	}
	
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.DPAD_UP) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.ACCEL_UP;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		} else if (keycode == Input.Keys.DPAD_DOWN) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.ACCEL_DOWN;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		} else if (keycode == Input.Keys.DPAD_LEFT) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.TURN_LEFT;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		} else if (keycode == Input.Keys.DPAD_RIGHT) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.TURN_RIGHT;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		}
		return false;
	}

	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.DPAD_UP) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.ACCEL_UP;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		} else if (keycode == Input.Keys.DPAD_DOWN) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.ACCEL_UP;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		} else if (keycode == Input.Keys.DPAD_LEFT) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.STEER_STRAIGHT;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		} else if (keycode == Input.Keys.DPAD_RIGHT) {
			byte[] msg = new byte[1];
			msg[0] = FRMessageCodes.STEER_STRAIGHT;
			networkServices.sendReliableMessage(msg, serverID);
			return true;
		}
		return false;
	}
}
