package com.letsrace.game;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.letsrace.game.car.Car;
import com.letsrace.game.map.FRMapHandler;

public class GameWorld {
	World physicalWorld;
	HashMap<String, Player> players;
	String arenaName;
	FRMapHandler mapHandler;
	int numberOfPlayers;

	public GameWorld(String arenaName) {
		this.arenaName = arenaName;
		this.physicalWorld = new World(new Vector2(0.0f, 0.0f), true);
		this.mapHandler = new FRMapHandler(physicalWorld, arenaName);
		this.players = new HashMap<String, Player>();
		this.numberOfPlayers = 0;
	}

	public void addPlayer(String id,String name, int playerType) {
		if(!players.containsKey(id)){
			Player newPlayer = new Player();
			newPlayer.name = name;
			newPlayer.playerType=playerType;
			newPlayer.car=new Car(physicalWorld, 3, 4, mapHandler.getInitialPositionForNumber(numberOfPlayers), 0.0f, 40, 20,60, "hummer");
			players.put(id, new Player());
			numberOfPlayers+=1;
		}
	}
	
}
