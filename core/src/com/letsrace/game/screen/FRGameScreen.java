package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRGameRenderer;
import com.letsrace.game.FRGameWorld;
import com.letsrace.game.LetsRace;
import com.letsrace.game.input.FRCarKeyboardInputHandler;
import com.letsrace.game.input.FRInputAdapter;
import com.letsrace.game.input.FRNetworkInputHandler;

public class FRGameScreen extends ScreenAdapter{
	public LetsRace gameRef;
	public FRGameRenderer renderer;
	public FRGameWorld gameWorldRef;
	public final boolean debug = true;
	public FRInputAdapter adapter;
	public FRGameScreen(LetsRace letsRace) {
		if(!debug){
			Gdx.app.log(FRConstants.TAG, "GameScreen(): Constructor");
			this.gameRef = letsRace;
			gameWorldRef = gameRef.client.gameWorld;
			gameRef.stage.clear();
			renderer = new FRGameRenderer(gameRef.myPlayerNo, gameWorldRef, gameRef.stage.getBatch());
			adapter = new FRNetworkInputHandler(gameRef.googleServices, gameRef.client.serverID);
			Gdx.input.setInputProcessor(adapter);
		}else{
			Gdx.app.log(FRConstants.TAG, "GameScreen(): Constructor");
			this.gameRef = letsRace;
			gameWorldRef = new FRGameWorld();
			gameWorldRef.carHandler.addCar(0, 0);
			renderer = new FRGameRenderer(0, gameWorldRef, gameRef.batch);
			adapter = new FRCarKeyboardInputHandler(gameWorldRef.carHandler.cars.get(0));
			Gdx.input.setInputProcessor(adapter);
		}
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
		gameWorldRef.update(delta);
	}

	@Override
	public void pause () {
		//TODO Add pause implementation
	}

	@Override
	public void resume () {
		//TODO Add pause resume implementation
	}
}
