package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.letsrace.game.FRConstants;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.LetsRace;
import com.letsrace.game.network.FRMessageCodes;

public class FRCarSelectScreen extends ScreenAdapter {
	private LetsRace gameRef;

	public FRCarSelectScreen(LetsRace game){
		Gdx.app.log(FRConstants.TAG, "Car-Select: Constructor");
		gameRef = game;
		gameRef.stage.clear();
		Gdx.input.setInputProcessor(gameRef.stage);
		Image image = new Image(gameRef.skin.getDrawable("wait-screen-back"));
		image.setWidth(Gdx.graphics.getWidth());
		image.setHeight(Gdx.graphics.getHeight());
		Image name = new Image(gameRef.skin.getDrawable("car-selection-name"));
		name.setWidth(400*FRConstants.GUI_SCALE_WIDTH);
		name.setHeight(50*FRConstants.GUI_SCALE_HEIGHT);
		name.setPosition(36*FRConstants.GUI_SCALE_WIDTH, (Gdx.graphics.getHeight()-30)*FRConstants.GUI_SCALE_HEIGHT);
		Image character = new Image(gameRef.skin.getDrawable("car-selection-char"));
		character.setWidth(230*FRConstants.GUI_SCALE_WIDTH);
		character.setHeight(250*FRConstants.GUI_SCALE_HEIGHT);
		character.setPosition(36*FRConstants.GUI_SCALE_WIDTH, (Gdx.graphics.getHeight()-96)*FRConstants.GUI_SCALE_HEIGHT);
		Image stats = new Image(gameRef.skin.getDrawable("car-selection-stats"));
		stats.setWidth(150*FRConstants.GUI_SCALE_WIDTH);
		stats.setHeight(250*FRConstants.GUI_SCALE_WIDTH);
		stats.setPosition(286*FRConstants.GUI_SCALE_WIDTH, (Gdx.graphics.getHeight()-96)*FRConstants.GUI_SCALE_HEIGHT);
		Image car = new Image(gameRef.skin.getDrawable("car-selection-car"));
		car.setWidth(400*FRConstants.GUI_SCALE_WIDTH);
		car.setHeight(400*FRConstants.GUI_SCALE_WIDTH);
		car.setPosition(36*FRConstants.GUI_SCALE_WIDTH, (Gdx.graphics.getHeight()-364)*FRConstants.GUI_SCALE_HEIGHT);
		car.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				byte[] msg = new byte[1];
				msg[0] = FRMessageCodes.SELECTED_CAR_0;
				gameRef.googleServices.sendReliableMessage(msg, gameRef.client.serverID);
				gameRef.moveToScreen(GameState.WAIT);
			}
		});
		gameRef.stage.addActor(image);
		gameRef.stage.addActor(name);
		gameRef.stage.addActor(character);
		gameRef.stage.addActor(stats);
		gameRef.stage.addActor(car);
	}

	public void show() {
		Gdx.app.log(FRConstants.TAG, "Car-Select: Show()");
		// Throwing stuff in constructor as loading stuff in show() causes black
		// screen
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameRef.stage.draw();
	}

	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Car-Select: Dispose()");
	}

}
