package com.letsrace.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.letsrace.game.car.FRCarHandler;
import com.letsrace.game.map.FRMapHandler;

public class FRGameWorld {
	public World physicalWorld;
	public FRMapHandler mapHandler;
	public FRCarHandler carHandler;
	
	public FRGameWorld(){
		physicalWorld = new World(new Vector2(0.0f, 0.0f), true);
		mapHandler = new FRMapHandler(physicalWorld);
		carHandler = new FRCarHandler(physicalWorld, mapHandler.initialPositionMarkers);
	}
	
	public void setupPrimaryContactListener(){
		//mapHandler.setupContactListener(carHandler.currentPlayerCar.body);
	}
	
	public void setupAllContactListeners(){
		
	}
	
	public void update(float delta) {
		carHandler.update(delta);
		physicalWorld.step(1 / 60f, 6, 2);
		physicalWorld.clearForces();
	}
	
}
