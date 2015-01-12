package com.letsrace.game.unused;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.letsrace.game.Animation;

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

	public static TextureAtlas arenaScreenAtlas;
	public static TextureAtlas playerAsset;
	public static TextureAtlas carScreenAtlas;
	public static Animation explosion;

	public static Sprite cannister;
	public static Sprite vaderFullCar;
	public static Sprite firedMissile;
	public static Sprite vaderCarBody;
	public static Animation wheel;

	public static Sprite powerBar;
	public static Sprite accnBar;
	public static Sprite steeringBar;
	
	public static void load() {
		uiButtonsAtlas = new TextureAtlas("ui/ui_icons.pack");
		playerAsset = new TextureAtlas("ui/playerAssetsAtlas.txt");
		carScreenAtlas = new TextureAtlas("ui/carScreenAtlas.txt");
		powerBar = carScreenAtlas.createSprite("powerBar");
		steeringBar=carScreenAtlas.createSprite("steeringBar");
		accnBar=carScreenAtlas.createSprite("accelerationBar");
		explosion = new Animation(false, 1 / 60f, playerAsset, "explosion", 41);
		cannister = playerAsset.createSprite("cannister", 1);
		vaderFullCar = playerAsset.createSprite("vaderCar_NoWeapon");
		firedMissile = playerAsset.createSprite("missileFired");
		vaderCarBody = playerAsset.createSprite("vaderCarBody");
		wheel = new Animation(true, 1 / 7f, playerAsset, "wheel", 2);
		/*
		 * singleplayerButton =
		 * uiButtonsAtlas.createSprite("singlePlayerButton"); multiplayerButton
		 * = uiButtonsAtlas.createSprite("multiplayerButton"); quickraceButton =
		 * uiButtonsAtlas.createSprite("quickRaceButton"); invitefriendsButton =
		 * uiButtonsAtlas .createSprite("inviteFriendsButton");
		 * checkInvitesButton =
		 * uiButtonsAtlas.createSprite("checkInvitesButton"); settingsIcon =
		 * uiButtonsAtlas.createSprite("settingsIcon"); frIcon =
		 * uiButtonsAtlas.createSprite("fr."); background =
		 * uiButtonsAtlas.createSprite("background"); signIn
		 * =uiButtonsAtlas.createSprite("signIn"); signInPressed =
		 * uiButtonsAtlas.createSprite("signInPressed");
		 */
		background = uiButtonsAtlas.createSprite("background");
		arenaScreenAtlas = new TextureAtlas("ui/arenaScreenUI.txt");
	}

	public static void dispose() {
		if (uiButtonsAtlas != null)
			uiButtonsAtlas.dispose();
	}
}
