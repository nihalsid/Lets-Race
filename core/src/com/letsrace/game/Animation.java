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
		this.timeBetweenFrames = timeBetweenFrames;
		this.length = length;
		currentFrame = 1;
	}

	public void update(float delta) {
		timeElapsed += delta;
		if (timeElapsed > timeBetweenFrames) {
			timeElapsed = 0.0f;
			if (isLooping) {
				currentFrame = 1 + currentFrame % length;
			} else {
				currentFrame = Math.min(currentFrame + 1, length);
			}
		}
	}

	public Sprite getCurrentSprite() {
		return atlas.createSprite(sprtiteName, currentFrame);
	}

}
