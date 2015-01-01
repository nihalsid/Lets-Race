package com.letsrace.game.unused;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class FRAssets {
	public static TextureAtlas uiButtonsAtlas;
	public static Sprite singleplayerButton;
	public static Sprite multiplayerButton;
	public static Sprite quickraceButton;
	public static Sprite invitefriendsButton;
	public static Sprite checkInvitesButton;
	public static Sprite settingsIcon;
	public static Sprite frIcon;
	public static Sprite background;
	public static Sprite signIn;
	public static Sprite signInPressed;
	
	

	public static void load() {
		uiButtonsAtlas = new TextureAtlas("ui/ui_icons.txt");
		singleplayerButton = uiButtonsAtlas.createSprite("singlePlayerButton");
		multiplayerButton = uiButtonsAtlas.createSprite("multiplayerButton");
		quickraceButton = uiButtonsAtlas.createSprite("quickRaceButton");
		invitefriendsButton = uiButtonsAtlas
				.createSprite("inviteFriendsButton");
		checkInvitesButton = uiButtonsAtlas.createSprite("checkInvitesButton");
		settingsIcon = uiButtonsAtlas.createSprite("settingsIcon");
		frIcon = uiButtonsAtlas.createSprite("fr.");
		background = uiButtonsAtlas.createSprite("background");
		signIn  =uiButtonsAtlas.createSprite("signIn");
		signInPressed = uiButtonsAtlas.createSprite("signInPressed");
	}

	public static void dispose() {
		if (uiButtonsAtlas != null)
			uiButtonsAtlas.dispose();
	}
}
