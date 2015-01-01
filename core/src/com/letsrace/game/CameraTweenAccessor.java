package com.letsrace.game;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.Camera;

public class CameraTweenAccessor implements TweenAccessor<Camera> {

	public static final int POSITION = 1;

	@Override
	public int getValues(Camera cam, int tweenType,
			float[] returnValues) {
		switch (tweenType) {
		case POSITION:
			returnValues[0] = cam.position.x;
			returnValues[1] = cam.position.y;
			return 2;
		default:
			return -1;
		}
	}

	@Override
	public void setValues(Camera cam, int tweenType,
			float[] newValues) {
		switch (tweenType) {
		case POSITION:
			cam.position.x = newValues[0];
			cam.position.y = newValues[1];
			cam.update();
		default:
			return;
		}
	}

}
