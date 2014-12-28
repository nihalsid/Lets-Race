package com.letsrace.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.letsrace.game.car.Car;
import com.letsrace.game.car.Car.Accel;
import com.letsrace.game.car.Car.Steer;

public class FRCarKeyboardInputHandler extends InputAdapter{
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
