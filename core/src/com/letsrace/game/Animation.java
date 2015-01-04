package com.letsrace.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Animation {
	boolean isLooping;
	float timeElapsed;
	String sprtiteName;
	float timeBetweenFrames;
	TextureAtlas atlas;
	int currentFrame;
	int length;

	public Animation(boolean isLooping, float timeBetweenFrames,
			TextureAtlas atlas, String spriteName, int length) {
		this.isLooping = isLooping;
		this.sprtiteName = spriteName;
		this.atlas = atlas;
		this.timeBetweenFrames = 0.0f;
		this.length = length;
	}

	public void update(float delta) {
		timeElapsed += delta;
		if (timeElapsed > timeBetweenFrames) {
			timeElapsed = 0.0f;
			currentFrame = currentFrame + 1;
			if (isLooping) {
				currentFrame = currentFrame % length;
			} else {
				currentFrame = Math.max(currentFrame, length);
			}
		}
	}
	
	public Sprite getCurrentSprite(){
		return atlas.createSprite(sprtiteName, currentFrame);
	}

}
