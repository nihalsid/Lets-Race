package com.letsrace.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.network.FRGameClient;
import com.letsrace.game.network.FRGameServer;
import com.letsrace.game.network.FRGoogleServices;
import com.letsrace.game.network.FRNetwork;
import com.letsrace.game.screen.CarSelectScreen;
import com.letsrace.game.screen.FRArenaSelectScreen;
import com.letsrace.game.screen.FRGameScreen;
import com.letsrace.game.screen.FRMenuScreen;
import com.letsrace.game.screen.FRMultiplayerMenuScreen;
import com.letsrace.game.screen.FRSplashScreen;
import com.letsrace.game.screen.FRWaitScreen;
import com.letsrace.game.screen.FRWaitingForPlayerScreen;
import com.letsrace.game.screen.GameScreen;
import com.letsrace.game.unused.FRAssets;

public class LetsRace extends Game {
	public GameState gameState;
	public BitmapFont font;
	public FRGoogleServices googleServices;
	public Stage stage;
	public Camera guicam;
	public SpriteBatch batch;
	public Skin skin;
	public HashMap<String, Integer> playerNumber;
	public int myPlayerNo;
	public FRGameClient client;
	public boolean multiplayer;
	public FRNetwork network;
	public String serverId;
	public String myId;
	public String arenaName;

	public GameWorld serverWorld;
	public GameWorld clientWorld;

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void setMyId(String myId) {
		this.myId = myId;
	}

	public void setArenaName(String arenaName) {
		this.arenaName = arenaName;
	}

	public boolean isServer() {
		if (serverId == null || myId == null) {
			return false;
		}
		return myId.equals(serverId);
	}

	public LetsRace(FRGoogleServices services) {
		googleServices = services;
		playerNumber = new HashMap<String, Integer>();
		network = new FRNetwork(this);
	}

	public void setUpWorlds() {
		if (isServer()) {
			serverWorld = new GameWorld();
			clientWorld = new GameWorld();
			for (String id : this.googleServices.getParticipantIds()) {
				serverWorld.addPlayer(id, id, -1);
				clientWorld.addPlayer(id, id, -1);
			}
		} else {
			clientWorld = new GameWorld();
			for (String id : googleServices.getParticipantIds()) {
				clientWorld.addPlayer(id, id, -1);
			}
		}
	}

	public void setUpArena(int arenaNumber) {
		if (isServer()) {
			this.serverWorld.arenaNumber = arenaNumber;
			this.serverWorld.setUpArena(arenaName);
			this.clientWorld.arenaNumber = arenaNumber;
			this.clientWorld.setUpArena(arenaName);
		} else {
			this.clientWorld.arenaNumber = arenaNumber;
			this.clientWorld.setUpArena(arenaName);
		}
	}

	public void setUpCars() {
		if (isServer()) {
			this.serverWorld.setUpCars();
			this.clientWorld.setUpCars();
		} else {
			this.clientWorld.setUpCars();
		}
	}

	public void decideOnServerAndStart() {
		ArrayList<String> list = googleServices.getParticipantIds();
		String[] participants = list.toArray(new String[list.size()]);
		Arrays.sort(participants);
		if (participants[0] == googleServices.getMyId()) {
			Gdx.app.log(FRConstants.TAG, "I am the server");
			FRGameServer server = new FRGameServer(this, participants);
			googleServices.setServerListener(server);
		}
		int ctr = 0;
		for (String s : participants) {
			playerNumber.put(s, ctr++);
		}
		myPlayerNo = playerNumber.get(googleServices.getMyId());
		Gdx.app.log(FRConstants.TAG, "I am the client - P" + myPlayerNo);
		client = new FRGameClient(this, participants[0]);
		Gdx.app.log(FRConstants.TAG, "Setting Client Listener");
		googleServices.setClientListener(client);
		Gdx.app.log(FRConstants.TAG, "Moving to car-select screen");
		moveToScreen(GameState.SELECT_CAR);
	}

	@Override
	public void create() {
		font = new BitmapFont();
		skin = new Skin(new TextureAtlas("ui/ui_icons.pack"));
		stage = new Stage();
		guicam = new OrthographicCamera();
		batch = new SpriteBatch();
		FRConstants.initializeDynamicConstants();
		FRAssets.load();
		moveToScreen(GameState.SPLASH);
	}

	@Override
	public void render() {
		super.render();
	}

	public void dispose() {
		stage.dispose();
		font.dispose();
	}

	public void moveToScreen(GameState screen) {
		switch (screen) {
		case GAME_SCREEN:
			gameState = GameState.GAME_SCREEN;
			setScreen(new GameScreen(this));
			break;
		case MENU:
			gameState = GameState.MENU;
			setScreen(new FRMenuScreen(this));
			break;
		case WAIT:
			gameState = GameState.WAIT;
			setScreen(new FRWaitScreen(this));
			break;
		case SELECT_CAR:
			gameState = GameState.SELECT_CAR;
			setScreen(new CarSelectScreen(this));
			break;
		case SPLASH:
			gameState = GameState.SPLASH;
			setScreen(new FRSplashScreen(this));
			break;
		case MULTIPLAYER_MENU:
			gameState = GameState.MULTIPLAYER_MENU;
			setScreen(new FRMultiplayerMenuScreen(this));
			break;
		case ARENA_SELECT:
			gameState = GameState.ARENA_SELECT;
			setScreen(new FRArenaSelectScreen(this));
			break;
		case PLAYER_WAITING:
			gameState = GameState.PLAYER_WAITING;
			setScreen(new FRWaitingForPlayerScreen(this));
			break;
		default:
			break;
		}
	}
}
