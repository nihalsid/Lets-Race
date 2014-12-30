package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.letsrace.game.FRGameRenderer;
import com.letsrace.game.FRGameWorld;
import com.letsrace.game.LetsRace;

public class FRGameScreen extends ScreenAdapter{
	public LetsRace gameRef;
	public FRGameRenderer renderer;
	public FRGameWorld gameWorldRef;
	
	public FRGameScreen(LetsRace letsRace) {
		this.gameRef = letsRace;
		gameWorldRef = gameRef.client.gameWorld;
		renderer = new FRGameRenderer(gameRef.myPlayerNo, gameWorldRef);
	}
	
	@Override
	public void render(float delta){
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

	@Override
	public void dispose () {
		//TODO Dispose allocated stuff
	}
}
