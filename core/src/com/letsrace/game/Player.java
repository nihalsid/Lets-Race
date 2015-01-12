package com.letsrace.game;

import com.letsrace.game.car.Car;

public class Player {
	public String name;
	public Car car;
	public int playerType;

	public enum PlayerState {
		HIT, BOOSTED, RUNNING
	}

	PlayerState state;

	public Player() {
		this.name = "MastBanda";
		this.car = null;
		this.playerType = 0;
		this.state = PlayerState.RUNNING;
	}

}
