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

public class FRMenuScreen extends ScreenAdapter {

	private LetsRace gameRef;

	boolean singlePlayerClicked = false;
	public FRMenuScreen(LetsRace letsRace) {
		Gdx.app.log(FRConstants.TAG, "Menu: Constructor");
		gameRef = letsRace;
		gameRef.stage.clear();
		
		Image singleplayer =  new Image(gameRef.skin.getDrawable("singlePlayerButton"));;
		Image multiplayer= new Image(gameRef.skin.getDrawable("multiplayerButton"));;
		Image setting= new Image(gameRef.skin.getDrawable("settingsIcon"));
		Image background= new Image(gameRef.skin.getDrawable("background"));

		background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		singleplayer.setSize(singleplayer.getWidth()*FRConstants.GUI_SCALE_WIDTH,singleplayer.getHeight()*FRConstants.GUI_SCALE_WIDTH);
		singleplayer.setPosition(0, 0.25f*Gdx.graphics.getHeight());
		singleplayer.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				singlePlayerClicked=true;
			}
		});
		
		multiplayer.setSize(multiplayer.getWidth()*FRConstants.GUI_SCALE_WIDTH,multiplayer.getHeight()*FRConstants.GUI_SCALE_WIDTH);
		multiplayer.setPosition(Gdx.graphics.getWidth() - multiplayer.getWidth(), 0.13f*Gdx.graphics.getHeight());
		multiplayer.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameRef.multiplayer = true;
				gameRef.moveToScreen(GameState.MULTIPLAYER_MENU);
			}
		});
		
		setting.setSize(setting.getWidth()*FRConstants.GUI_SCALE_WIDTH,setting.getHeight()*FRConstants.GUI_SCALE_WIDTH);
		setting.setPosition((0.2f*Gdx.graphics.getWidth())/6, (0.02f*Gdx.graphics.getHeight()));
		
		gameRef.stage.addActor(background);
		gameRef.stage.addActor(singleplayer);
		gameRef.stage.addActor(multiplayer);
		gameRef.stage.addActor(setting);
	}

	public void show() {
		Gdx.app.log(FRConstants.TAG, "Menu: Show()");
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(singlePlayerClicked){
			gameRef.multiplayer = false;
			gameRef.setMyId("local");
			gameRef.setServerId("local");
			gameRef.setUpWorlds();
			gameRef.moveToScreen(GameState.ARENA_SELECT);
		}
		gameRef.stage.draw();
	}

}