package com.letsrace.game;

import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.letsrace.game.car.Car;
import com.letsrace.game.map.FRMapHandler;

public class GameWorld {
	public World physicalWorld;
	public HashMap<String, Player> players;
	public String arenaName;
	public FRMapHandler mapHandler;
	public int numberOfPlayers;
	int arenaNumber;

	public GameWorld() {
		this.physicalWorld = new World(new Vector2(0.0f, 0.0f), true);
		this.players = new HashMap<String, Player>();
		this.numberOfPlayers = 0;
	}

	public void setUpArena(String arenaName) {
		mapHandler = new FRMapHandler(physicalWorld,
				"beach_track_draft_two.tmx");
	}

	public void addPlayer(String id, String name, int playerType) {
		if (!players.containsKey(id)) {
			Player newPlayer = new Player();
			newPlayer.name = name;
			newPlayer.playerType = playerType;
			players.put(id, new Player());
			numberOfPlayers += 1;
		}
	}

	public void setUpCars() {
		Set keySet = players.keySet();
		int index = 0;
		for (Object key : keySet) {
			Player player = players.get(key);
			Vector2 initialPosition = mapHandler
					.getInitialPositionForNumber(index);
			player.car = new Car(physicalWorld, 3, 4, initialPosition, 0.0f,
					40, 20, 60, "hummer");
			index += 1;
		}
	}

}
