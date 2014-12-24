package com.letsrace.game.car;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class FRCarHandler {
	
	public ArrayList<FRRaceCar> carList;
	public FRRaceCar currentPlayerCar;
	
	public FRCarHandler(World physicalWorld, Vector2 currentCarPosition){
		carList = new ArrayList<FRRaceCar>();
		currentPlayerCar = new FRHummer(physicalWorld, currentCarPosition);
		carList.add(currentPlayerCar);
	}

	public void update(float delta) {
		currentPlayerCar.update(delta);
	}

}
