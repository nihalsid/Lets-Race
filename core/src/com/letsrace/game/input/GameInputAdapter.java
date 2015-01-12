package com.letsrace.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.letsrace.game.FRConstants;
import com.letsrace.game.LetsRace;
import com.letsrace.game.Message;
import com.letsrace.game.car.Car.Accel;
import com.letsrace.game.car.Car.Steer;
import com.letsrace.game.network.FRMessageCodes;

public class GameInputAdapter extends InputAdapter {
	public LetsRace gameRef;

	public GameInputAdapter(LetsRace race) {
		this.gameRef = race;
	}

	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.DPAD_UP) {
			byte message[] = new byte[1];
			message[0] = FRMessageCodes.ACCELERATE;
			Gdx.app.log(FRConstants.TAG, "ACC");
			gameRef.network.sendToServer(new Message(message, gameRef.serverId));
			return true;
		} else if (keycode == Input.Keys.DPAD_DOWN) {
			byte message[] = new byte[1];
			Gdx.app.log(FRConstants.TAG, "BRA");
			message[0] = FRMessageCodes.BRAKE;
			gameRef.network.sendToServer(new Message(message, gameRef.serverId));
			return true;
		} else if (keycode == Input.Keys.DPAD_LEFT) {
			Gdx.app.log(FRConstants.TAG, "LE");
			byte message[] = new byte[1];
			message[0] = FRMessageCodes.MOVE_LEFT;
			gameRef.network.sendToServer(new Message(message, gameRef.serverId));
			return true;
		} else if (keycode == Input.Keys.DPAD_RIGHT) {
			Gdx.app.log(FRConstants.TAG, "RIGHt");
			byte message[] = new byte[1];
			message[0] = FRMessageCodes.MOVE_RIGHT;
			gameRef.network.sendToServer(new Message(message, gameRef.serverId));
			return true;
		}
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	public void handleAccelerometer() {
	}

	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.DPAD_UP) {
			byte message[] = new byte[1];
			message[0] = FRMessageCodes.ACCELERATE_NONE;
			Gdx.app.log(FRConstants.TAG, "ACC");
			gameRef.network.sendToServer(new Message(message, gameRef.serverId));
			return true;
		} else if (keycode == Input.Keys.DPAD_DOWN) {
			byte message[] = new byte[1];
			message[0] = FRMessageCodes.ACCELERATE_NONE;
			Gdx.app.log(FRConstants.TAG, "ACC");
			gameRef.network.sendToServer(new Message(message, gameRef.serverId));
			return true;
		} else if (keycode == Input.Keys.DPAD_LEFT) {
			byte message[] = new byte[1];
			message[0] = FRMessageCodes.STEER_NONE;
			Gdx.app.log(FRConstants.TAG, "ACC");
			gameRef.network.sendToServer(new Message(message, gameRef.serverId));
			return true;
		} else if (keycode == Input.Keys.DPAD_RIGHT) {
			byte message[] = new byte[1];
			message[0] = FRMessageCodes.STEER_NONE;
			Gdx.app.log(FRConstants.TAG, "ACC");
			gameRef.network.sendToServer(new Message(message, gameRef.serverId));
			return true;
		}
		return false;
	}
}
