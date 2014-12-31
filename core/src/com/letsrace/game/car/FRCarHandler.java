package com.letsrace.game.car;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class FRCarHandler {
	
	public HashMap<Integer, Car> cars;
	public World physicalWorld;
	public Vector2[] initialPositions;
	public FRCarHandler(World physicalWorld, Vector2[] initialPositions){
		this.physicalWorld = physicalWorld;
		this.initialPositions = initialPositions;
		cars = new HashMap<Integer, Car>();
	}

	public void addCar(int player, int carCode){
		cars.put(player, new Car(physicalWorld, 3, 4, initialPositions[player], 0.0f, 40, 20,60, "hummer"));
	}
	
	public void update(float delta) {
		for(Car car: cars.values())
			car.update(delta);
	}

}
