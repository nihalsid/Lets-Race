package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.letsrace.game.FRConstants;
import com.letsrace.game.LetsRace;

public class FRWaitScreen extends ScreenAdapter{
	private Stage stage;
	private Skin skin;
	private LetsRace gameRef;
	private final static float ROTATION = 40;
	private final static float DURATION = 0.65f;
	
	public FRWaitScreen(LetsRace letsRace) {
		Gdx.app.log(FRConstants.TAG, "Wait: Constructor");
		gameRef = letsRace;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(gameRef.textureAtlas);
		Image image = new Image(skin.getDrawable("wait-screen-back"));
		image.setWidth(Gdx.graphics.getWidth());
		image.setHeight(Gdx.graphics.getHeight());
		image.setZIndex(1);
		stage.addActor(image);
		Image steering = new Image(skin.getDrawable("wait-screen-steer"));
		steering.setWidth(210 * ((float)Gdx.graphics.getWidth()/FRConstants.GUI_WIDTH));
		steering.setHeight(210 * ((float)Gdx.graphics.getHeight()/FRConstants.GUI_HIEGHT));
		steering.setPosition(Gdx.graphics.getWidth()/2 - steering.getWidth()/2, Gdx.graphics.getHeight()/2  - steering.getHeight()/2);
		steering.setOrigin(steering.getWidth()/2,steering.getHeight()/2);
		steering.addAction(Actions.forever(Actions.sequence(Actions.rotateBy(ROTATION,DURATION),Actions.rotateBy(-2*ROTATION,DURATION*2),Actions.rotateBy(ROTATION,DURATION),Actions.rotateBy(ROTATION*4,DURATION),Actions.rotateBy(-ROTATION*4,DURATION))));
		stage.addActor(steering);		
	}
	public void show(){
		Gdx.app.log(FRConstants.TAG, "Wait: show()");
		// Throwing stuff in constructor as loading stuff in show() causes black screen
	}
	
	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameRef.batch.begin();
		stage.act(delta);
		stage.draw();
		gameRef.batch.end();
	}
	
	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Wait: dispose()");
		stage.dispose();
		skin.dispose();
	}
	
	

}
