package com.letsrace.game.car;

import com.badlogic.gdx.math.Rectangle;

public class CarRenderingProperties {
	public Rectangle carBounds;
	public Rectangle wheelOneBounds, wheelTwoBounds, wheelThreeBounds,
			wheelFourBounds;

	public CarRenderingProperties() {
		carBounds = new Rectangle();
		wheelOneBounds = new Rectangle();
		wheelTwoBounds = new Rectangle();
		wheelThreeBounds = new Rectangle();
		wheelFourBounds = new Rectangle();
	}
}
