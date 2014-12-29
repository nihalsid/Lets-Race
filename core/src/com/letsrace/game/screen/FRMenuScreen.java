package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.letsrace.game.FRConstants;
import com.letsrace.game.LetsRace;

public class FRMenuScreen extends ScreenAdapter {

	private Stage stage;
	private Skin skin;
	private LetsRace gameRef;
	private final static int TOTAL_BUTTONS_ON_SCREEN = 3;
	private final static int PADDING = 10;

	public FRMenuScreen(LetsRace letsRace) {
		Gdx.app.log(FRConstants.TAG, "Menu: Constructor");
		gameRef = letsRace;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(gameRef.textureAtlas);
		Image image = new Image(skin.getDrawable("menu-back"));
		image.setWidth(Gdx.graphics.getWidth());
		image.setHeight(Gdx.graphics.getHeight());
		stage.addActor(image);
		int buttonCtr = 3;
		stage.addActor(generateTextButton("Quick Game", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameRef.googleServices.startQuickGame();
			}
		}, --buttonCtr));
		stage.addActor(generateTextButton("Challenge friends",
				new ClickListener() {
					public void clicked(InputEvent event, float x, float y) {

					}
				}, --buttonCtr));
		stage.addActor(generateTextButton("Check invites", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {

			}
		}, --buttonCtr));
	}

	public void show() {
		Gdx.app.log(FRConstants.TAG, "Menu: Show()");
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
		Gdx.app.log(FRConstants.TAG, "Menu: Dispose()");
		stage.dispose();
		skin.dispose();
	}
	
	private TextButton generateTextButton(String text, ClickListener listener,
			int buttonNumber) {
		TextButton button = new TextButton(text, new TextButtonStyle(
				skin.getDrawable("button-up"), skin.getDrawable("button-down"),
				null, gameRef.font));
		button.addListener(listener);
		button.setWidth(280 * ((float) Gdx.graphics.getWidth() / FRConstants.GUI_WIDTH));
		button.setHeight(80 * ((float) Gdx.graphics.getHeight() / FRConstants.GUI_HIEGHT));
		button.setPosition(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2,
				Gdx.graphics.getHeight() / 2
						- (TOTAL_BUTTONS_ON_SCREEN / 2 - buttonNumber)
						* (button.getHeight() + PADDING));
		return button;
	}
}