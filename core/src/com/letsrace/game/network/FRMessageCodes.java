package com.letsrace.game.network;

public class FRMessageCodes {
	public static final byte SELECTED_CAR_0 = (byte) 0x80;
	public static final byte SELECTED_CAR_1 = (byte) 0x81;
	public static final byte SELECTED_CAR_2 = (byte) 0x82;
	public static final byte SELECTED_CAR_3 = (byte) 0x83;
	public static final byte PROCEED_TO_GAME_SCREEN = (byte) 0x08;
	public static final byte PING_DETECT_REQ = (byte) 0x78;
	public static final byte PING_DETECT_RES = (byte) 0xF8;
	public static final byte CAR_PICK_CONFIRMED_PLAYER_0 = (byte) 0x10;
	public static final byte CAR_PICK_CONFIRMED_PLAYER_1 = (byte) 0x11;
	public static final byte CAR_PICK_CONFIRMED_PLAYER_2 = (byte) 0x12;
	public static final byte CAR_PICK_CONFIRMED_PLAYER_3 = (byte) 0x13;
	public static final byte REPICK_CAR = (byte) 0x18;
	public static final byte RESYNC_HEAD = (byte) 0x20;
	public static final byte ACCEL_DOWN = (byte) 0xA8;
	public static final byte ACCEL_DOWN_PLAYER_0 = (byte) 0x28;
	public static final byte ACCEL_DOWN_PLAYER_1 = (byte) 0x29;
	public static final byte ACCEL_DOWN_PLAYER_2 = (byte) 0x2A;
	public static final byte ACCEL_DOWN_PLAYER_3 = (byte) 0x2B;
	public static final byte ACCEL_UP = (byte) 0xB0;
	public static final byte ACCEL_UP_PLAYER_0 = (byte) 0x30;
	public static final byte ACCEL_UP_PLAYER_1 = (byte) 0x31;
	public static final byte ACCEL_UP_PLAYER_2 = (byte) 0x32;
	public static final byte ACCEL_UP_PLAYER_3 = (byte) 0x33;
	public static final byte TURN_LEFT = (byte) 0xB8;
	public static final byte TURN_LEFT_PLAYER_0 = (byte) 0x38;
	public static final byte TURN_LEFT_PLAYER_1 = (byte) 0x39;
	public static final byte TURN_LEFT_PLAYER_2 = (byte) 0x3A;
	public static final byte TURN_LEFT_PLAYER_3 = (byte) 0x3B;
	public static final byte TURN_RIGHT = (byte) 0xC0;
	public static final byte TURN_RIGHT_PLAYER_0 = (byte) 0x40;
	public static final byte TURN_RIGHT_PLAYER_1 = (byte) 0x41;
	public static final byte TURN_RIGHT_PLAYER_2 = (byte) 0x42;
	public static final byte TURN_RIGHT_PLAYER_3 = (byte) 0x43;
	public static final byte STEER_STRAIGHT = (byte) 0xC8;
	public static final byte STEER_STRAIGHT_PLAYER_0 = (byte) 0x48;
	public static final byte STEER_STRAIGHT_PLAYER_1 = (byte) 0x49;
	public static final byte STEER_STRAIGHT_PLAYER_2 = (byte) 0x4A;
	public static final byte STEER_STRAIGHT_PLAYER_3 = (byte) 0x4B;
	public static final byte NO_ACCELERATE = (byte) 0xD0;

	public static final byte NO_ACCELERATE_PLAYER_0 = (byte) 0x50;
	public static final byte NO_ACCELERATE_PLAYER_1 = (byte) 0x51;
	public static final byte NO_ACCELERATE_PLAYER_2 = (byte) 0x52;
	public static final byte NO_ACCELERATE_PLAYER_3 = (byte) 0x53;

	public static final byte SIGN_IN_SUCCESSFUL = (byte) 0xAA;
	public static final byte SIGN_IN_FAILED = (byte) 0xAB;

	public static final byte ARENA_SELECTED = (byte) 0xAC;
	public static final byte SELECT_CAR = (byte) 0xAD;
	public static final byte CAR_SELECTED = (byte) 0xAE;
	public static final byte CAR_SELECTION_CONFIRMED = (byte) 0xAE;

	public static final byte MOVE_LEFT = (byte) 0xBA;
	public static final byte MOVE_RIGHT = (byte) 0xBB;
	public static final byte ACCELERATE = (byte) 0xBC;
	public static final byte BRAKE = (byte) 0xBD;

	public static final byte SYNC_CARS = (byte) 0xBE;
	public static final byte STEER_NONE = (byte)0xBF;
	public static final byte ACCELERATE_NONE = (byte)0xCA;
	
	public static byte extractHeaderExtField(byte msg) {
		return (byte) (msg & 0x07);
	}

	public static byte convertToCorrespondingPlayerMessageCode(byte msg,
			int playerNumber) {
		return (byte) ((msg | 0x80) | (playerNumber & 0x07));
	}
}
