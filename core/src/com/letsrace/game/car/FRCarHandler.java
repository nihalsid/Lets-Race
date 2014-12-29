package com.letsrace.game.car;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class FRCarHandler {
	
	public ArrayList<Car> carList;
	public Car currentPlayerCar;
	
	public FRCarHandler(World physicalWorld, Vector2 currentCarPosition){
		carList = new ArrayList<Car>();
		currentPlayerCar = new Car(physicalWorld, 3, 4, currentCarPosition, 0.0f, 40, 20,60, "hummer");
		carList.add(currentPlayerCar);
	}

	public void update(float delta) {
		for(Car car: carList)
			car.update(delta);
	}

}
