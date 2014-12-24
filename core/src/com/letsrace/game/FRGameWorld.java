package com.letsrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.letsrace.game.car.FRCarHandler;
import com.letsrace.game.input.FRCarKeyboardInputHandler;
import com.letsrace.game.map.FRMapHandler;

public class FRGameWorld {
	public World physicalWorld;
	public FRMapHandler mapHandler;
	public FRCarHandler carHandler;
	
	public FRGameWorld(){
		physicalWorld = new World(new Vector2(0.0f, 0.0f), true);
		mapHandler = new FRMapHandler(physicalWorld);
		carHandler = new FRCarHandler(physicalWorld, mapHandler.initialPositionMarker);
		mapHandler.setupContactListener(carHandler.currentPlayerCar.carCore.body);
		Gdx.input.setInputProcessor(new FRCarKeyboardInputHandler(carHandler.currentPlayerCar.carCore));
	}
	
	public void update(float delta) {
		carHandler.update(delta);
		physicalWorld.step(1 / 60f, 6, 2);
		physicalWorld.clearForces();
	}
	
}
