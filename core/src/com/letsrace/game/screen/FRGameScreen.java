package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRGameRenderer;
import com.letsrace.game.FRGameWorld;
import com.letsrace.game.LetsRace;
import com.letsrace.game.input.FRInputAdapter;
import com.letsrace.game.input.FRNetworkInputHandler;

public class FRGameScreen extends ScreenAdapter{
	public LetsRace gameRef;
	public FRGameRenderer renderer;
	public FRGameWorld gameWorldRef;
	public FRInputAdapter adapter;
	public FRGameScreen(LetsRace letsRace) {
			Gdx.app.log(FRConstants.TAG, "GameScreen(): Constructor");
			this.gameRef = letsRace;
			gameWorldRef = gameRef.client.gameWorld;
			gameRef.stage.clear();
			renderer = new FRGameRenderer(gameRef.myPlayerNo, gameWorldRef,gameRef.server.gameWorld, gameRef.stage.getBatch());
			adapter = new FRNetworkInputHandler(gameRef.googleServices, gameRef.client.serverID, gameRef.client.gameWorld.carHandler.cars.get(gameRef.myPlayerNo));
			Gdx.input.setInputProcessor(adapter);
	}
	
	@Override
	public void render(float delta){
		adapter.handleAccelerometer();
		update(delta);
		draw();
	}
	
	private void draw() {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.render();
	}

	private void update(float delta) {
		gameWorldRef.update(1f/FRConstants.SYNC_PACKETS_PER_SEC);
		gameRef.client.update();
	}

}
