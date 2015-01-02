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
import com.letsrace.game.FRConstants.GameState;

public class FRMultiplayerMenuScreen extends ScreenAdapter {
	LetsRace gameRef;
	Image quickrace;
	Image inviteplayers;
	Image checkinvites;
	TextButton signIn;
	
	public FRMultiplayerMenuScreen(LetsRace letsRace) {
		Gdx.app.log(FRConstants.TAG, "Menu: Constructor");
		gameRef = letsRace;
		gameRef.stage.clear();
		
		quickrace = new Image(gameRef.skin.getDrawable("quickRaceButton"));
		inviteplayers=new Image(gameRef.skin.getDrawable("inviteFriendsButton"));
		checkinvites=new Image(gameRef.skin.getDrawable("checkInvitesButton"));;
		Image background=new Image(gameRef.skin.getDrawable("background"));;
		TextButtonStyle style = new TextButtonStyle(gameRef.skin.getDrawable("signIn"), gameRef.skin.getDrawable("signInPressed"), null, gameRef.font);
		signIn=new TextButton("", style);
		
		signIn.setSize(signIn.getWidth()*FRConstants.GUI_SCALE_WIDTH,signIn.getHeight()*FRConstants.GUI_SCALE_WIDTH);
		signIn.setPosition((1.375f*Gdx.graphics.getWidth())/6, 0.3f*Gdx.graphics.getHeight());
		signIn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameRef.googleServices.initiateSignIn();
			}
		});
		
		checkinvites.setSize(checkinvites.getWidth()*FRConstants.GUI_SCALE_WIDTH,checkinvites.getHeight()*FRConstants.GUI_SCALE_WIDTH);
		checkinvites.setPosition(0, 0.05f*Gdx.graphics.getHeight());
		
		inviteplayers.setSize(inviteplayers.getWidth()*FRConstants.GUI_SCALE_WIDTH,inviteplayers.getHeight()*FRConstants.GUI_SCALE_WIDTH);
		inviteplayers.setPosition(Gdx.graphics.getWidth()-inviteplayers.getWidth(), 0.17f*Gdx.graphics.getHeight());

		quickrace.setSize(quickrace.getWidth()*FRConstants.GUI_SCALE_WIDTH,quickrace.getHeight()*FRConstants.GUI_SCALE_WIDTH);
		quickrace.setPosition(0, 0.29f*Gdx.graphics.getHeight());
		quickrace.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameRef.moveToScreen(GameState.ARENA_SELECT);
				//gameRef.googleServices.startQuickGame();
			}
		});
	
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		gameRef.stage.addActor(background);
		if(gameRef.googleServices.isSignedIn()){
			gameRef.stage.addActor(checkinvites);
			gameRef.stage.addActor(quickrace);
			gameRef.stage.addActor(inviteplayers);
		}
		else{
			gameRef.stage.addActor(signIn);
		}
	}

	public void show() {
		Gdx.app.log(FRConstants.TAG, "Menu: Show()");
	}

	@Override
	public void render(float delta) {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameRef.stage.draw();
	}
	
	public void enableSignedInButtons(){
		signIn.remove();
		gameRef.stage.addActor(checkinvites);
		gameRef.stage.addActor(quickrace);
		gameRef.stage.addActor(inviteplayers);
	}

}