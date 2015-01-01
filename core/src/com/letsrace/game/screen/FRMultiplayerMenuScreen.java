package com.letsrace.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.letsrace.game.FRConstants;
import com.letsrace.game.LetsRace;
import com.letsrace.game.OverlapTester;

public class FRMultiplayerMenuScreen extends ScreenAdapter {

	private LetsRace gameRef;
	SpriteBatch batch;
	Sprite quickrace;
	Sprite inviteplayers;
	Sprite checkinvites;
	Sprite background;
	Sprite signIn;
	Sprite signInPressed;

	Rectangle qrButton;
	Rectangle inviteButton;
	Rectangle checkInvitesButton;
	Rectangle signInButton;

	Camera camera;
	Vector3 touchPoint;

	public FRMultiplayerMenuScreen(LetsRace letsRace) {
		Gdx.app.log(FRConstants.TAG, "Menu: Constructor");
		gameRef = letsRace;
		batch = new SpriteBatch();
		quickrace = FRAssets.quickraceButton;
		inviteplayers = FRAssets.invitefriendsButton;
		checkinvites = FRAssets.checkInvitesButton;
		background = FRAssets.background;
		signIn = FRAssets.signIn;
		signInPressed = FRAssets.signInPressed;

		camera = new OrthographicCamera(6, 10);
		camera.position.set(camera.viewportWidth / 2f,
				camera.viewportHeight / 2f, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		signIn.setPosition(1.375f, 3f);
		signIn.setSize(3.25f, 1.25f);
		signInButton = new Rectangle(1.375f, 3f, 3.25f, 1.25f);

		checkinvites.setPosition(0, 0.5f);
		checkinvites.setSize(5, 1);
		checkInvitesButton = new Rectangle(0, 0.5f, 5, 1);

		inviteplayers.setPosition(1, 1.7f);
		inviteplayers.setSize(5, 1);
		inviteButton = new Rectangle(1, 1.7f, 5, 1);

		quickrace.setPosition(0, 2.9f);
		quickrace.setSize(5, 1);
		qrButton = new Rectangle(0, 2.9f, 5, 1);

		background.setPosition(0, 0);
		background.setSize(6, 10);

		touchPoint = new Vector3();
	}

	public void show() {
		Gdx.app.log(FRConstants.TAG, "Menu: Show()");
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!gameRef.googleServices.isSignedIn()) {
			updateSignedIn(delta);
			renderSignedIn(delta);
		} else {
			updateMenu(delta);
			renderMenu(delta);
		}
	}

	private void updateMenu(float delta) {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (OverlapTester.pointInRectangle(qrButton, touchPoint.x,
					touchPoint.y)) {
				Gdx.app.log(FRConstants.TAG, "SinglePLayer Button Clicked");
				quickRaceButtonClicked();
			}
			if (OverlapTester.pointInRectangle(inviteButton, touchPoint.x,
					touchPoint.y)) {
				Gdx.app.log(FRConstants.TAG, "Multiplayer Button Clicked");
				inviteFriendsButtonClicked();
			}
			if (OverlapTester.pointInRectangle(checkInvitesButton,
					touchPoint.x, touchPoint.y)) {
				Gdx.app.log(FRConstants.TAG, "Settings Button Clicked");
				checkInvitesButtonClicked();
			}
		}
	}

	private void updateSignedIn(float delta) {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (OverlapTester.pointInRectangle(signInButton, touchPoint.x,
					touchPoint.y)) {
				Gdx.app.log(FRConstants.TAG, "Sign In Button Clicked");
				signInButtonClicked();
			}
		}

	}

	private void renderMenu(float delta) {
		batch.begin();
		background.draw(batch);
		quickrace.draw(batch);
		inviteplayers.draw(batch);
		checkinvites.draw(batch);
		batch.end();
	}

	private void renderSignedIn(float delta) {
		batch.begin();
		background.draw(batch);
		signIn.draw(batch);
		batch.end();
	}

	public void dispose() {
		Gdx.app.log(FRConstants.TAG, "Menu: Dispose()");
	}

	private void quickRaceButtonClicked() {

	}

	private void inviteFriendsButtonClicked() {

	}

	private void checkInvitesButtonClicked() {

	}

	private void signInButtonClicked() {
		gameRef.googleServices.initiateSignIn();
	}

}