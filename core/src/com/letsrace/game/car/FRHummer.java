package com.letsrace.game.car;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class FRHummer extends FRRaceCar {
	public FRHummer(World physicalWorld, Vector2 position){
		super(physicalWorld, 3, 4, position, 0.0f, 40, 20,60);
	}
	
}
