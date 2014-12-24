package com.letsrace.game.car;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class FRRaceCar {
	public Car carCore;

	public FRRaceCar(World world, float width, float length, Vector2 position,
			float angle, float power, float maxSteerAngle, float maxSpeed) {
		carCore = new Car(world, width, length, position, angle, power,
				maxSteerAngle, maxSpeed);
	}

	public void update(float delta) {
		carCore.update(delta);
	}
}
