package com.letsrace.game;

import com.badlogic.gdx.Gdx;

public class FRConstants {
	public final static int PIXELS_PER_UNIT = 16;
	public enum GameState {
		SPLASH,SPLASH_SIGN_IN, MENU, WAIT, SELECT_CAR, GAME_SCREEN, CAR_SELECT,MULTIPLAYER_MENU,ARENA_SELECT};
	public final static int GUI_WIDTH = 480;
	public final static int GUI_HIEGHT = 800;
	public static float GUI_SCALE_WIDTH;
	public static final String TAG = "Lets-Race!";
	public static void initializeDynamicConstants(){
		GUI_SCALE_WIDTH = ((float)Gdx.graphics.getWidth()/GUI_WIDTH);
	}
}
