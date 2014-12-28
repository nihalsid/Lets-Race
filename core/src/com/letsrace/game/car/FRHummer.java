package com.letsrace.game.car;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class FRHummer extends FRRaceCar {
	public Sprite carSpriteNormal;
	
	public FRHummer(World physicalWorld, Vector2 position){
		super(physicalWorld, 3, 4, position, 0.0f, 40, 20,60, "hummer");
		carSpriteNormal = new Sprite(new Texture(Gdx.files.internal("hummer-normal.png")));
	}

	@Override
	public void renderNormalState() {
		// TODO Auto-generated method stub
		
	}
	
}
