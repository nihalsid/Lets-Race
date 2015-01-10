package com.letsrace.game.network;

import com.letsrace.game.GameWorld;
import com.letsrace.game.LetsRace;

public class Server {
	GameWorld world;
	LetsRace gameRef;
	enum ServerState {
		WAITING_FOR_ARENA_SELECT,
		CAR_SELECT,		
	}
}
