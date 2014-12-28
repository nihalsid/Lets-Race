package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.letsrace.game.FRConstants;
import com.letsrace.game.LetsRace;

public class FRSplashScreen extends ScreenAdapter{
	private Stage stage;
	private Skin skin;
	private LetsRace gameRef;
	
	public FRSplashScreen(LetsRace game){
		Gdx.app.log(FRConstants.TAG, "Splash: Constructor");
		gameRef = game;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(gameRef.textureAtlas);
		Image image = new Image(skin.getDrawable("wait-screen-back"));
		image.setWidth(Gdx.graphics.getWidth());
		image.setHeight(Gdx.graphics.getHeight());
		LabelStyle style = new LabelStyle(gameRef.font, Color.WHITE);
		Label label = new Label("Signing in ...", style);
		label.setPosition((Gdx.graphics.getWidth()-label.getWidth())/2,(Gdx.graphics.getHeight())/2+label.getHeight());
		stage.addActor(image);
		stage.addActor(label);
	}
	
	public void show(){
		Gdx.app.log(FRConstants.TAG, "Splash: Show()");
		// Throwing stuff in constructor as loading stuff in show() causes black screen
		gameRef.googleServices.initiateSignIn();
	}
	
	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameRef.batch.begin();
		stage.draw();
		gameRef.batch.end();
	}
	
	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Splash: Dispose()");
		stage.dispose();
		skin.dispose();
	}
	
	
}
