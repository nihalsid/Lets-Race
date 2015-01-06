package com.letsrace.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.letsrace.game.car.Car;
import com.letsrace.game.car.Car.Accel;
import com.letsrace.game.car.Car.Steer;

public class FRCarKeyboardInputHandler extends FRInputAdapter{
	public Car carRef;
	
	public FRCarKeyboardInputHandler(Car car){
		this.carRef = car;
	}
	
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.DPAD_UP) {
			carRef.accelerate = Accel.ACCELERATE;
			return true;
		} else if (keycode == Input.Keys.DPAD_DOWN) {
			carRef.accelerate = Accel.BRAKE;
			return true;
		} else if (keycode == Input.Keys.DPAD_LEFT) {
			carRef.steer = Steer.LEFT;
			return true;
		} else if (keycode == Input.Keys.DPAD_RIGHT) {
			carRef.steer = Steer.RIGHT;
			return true;
		}
		return false;
	}
	
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		carRef.accelerate = Accel.ACCELERATE;
		return true;	}

	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		carRef.accelerate = Accel.BRAKE;
		return true;
	}

	public void handleAccelerometer(){
		final float NOISE = 2f;
		float x = Gdx.input.getAccelerometerX();
		if (x>=NOISE && carRef.steer!=Steer.RIGHT){
			carRef.steer = Steer.RIGHT;
		}
		if(x<=-NOISE&&carRef.steer!=Steer.LEFT){
			carRef.steer = Steer.LEFT;
		}
		if(x>-NOISE&&x<NOISE&&carRef.steer!=Steer.NONE){
			carRef.steer = Steer.NONE;
		}
	}
	
	
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.DPAD_UP) {
			carRef.accelerate = Accel.ACCELERATE;
			return true;
		} else if (keycode == Input.Keys.DPAD_DOWN) {
			carRef.accelerate = Accel.ACCELERATE;
			return true;
		} else if (keycode == Input.Keys.DPAD_LEFT) {
			carRef.steer = Steer.NONE;
			return true;
		} else if (keycode == Input.Keys.DPAD_RIGHT) {
			carRef.steer = Steer.NONE;
			return true;
		}
		return false;
	}
}
