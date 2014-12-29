package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.letsrace.game.FRConstants;
import com.letsrace.game.LetsRace;

public class FRMenuScreen extends ScreenAdapter {

	private LetsRace gameRef;
	private final static int TOTAL_BUTTONS_ON_SCREEN = 3;
	private final static int PADDING = 10;

	public FRMenuScreen(LetsRace letsRace) {
		Gdx.app.log(FRConstants.TAG, "Menu: Constructor");
		gameRef = letsRace;
		gameRef.stage.clear();
		Gdx.input.setInputProcessor(letsRace.stage);
		Image image = new Image(letsRace.skin.getDrawable("menu-back"));
		image.setWidth(Gdx.graphics.getWidth());
		image.setHeight(Gdx.graphics.getHeight());
		letsRace.stage.addActor(image);
		int buttonCtr = 3;
		letsRace.stage.addActor(generateTextButton("Quick Game", new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				gameRef.googleServices.startQuickGame();
			}
		}, --buttonCtr));
		letsRace.stage.addActor(generateTextButton("Challenge friends",
				new ClickListener() {
					public void clicked(InputEvent event, float x, float y) {

					}
				}, --buttonCtr));
		letsRace.stage.addActor(generateTextButton("Check invites", new ClickListener() {
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
		gameRef.stage.act(delta);
		gameRef.stage.draw();
	}

	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Menu: Dispose()");
	}
	
	private TextButton generateTextButton(String text, ClickListener listener,
			int buttonNumber) {
		TextButton button = new TextButton(text, new TextButtonStyle(
				gameRef.skin.getDrawable("button-up"), gameRef.skin.getDrawable("button-down"),
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