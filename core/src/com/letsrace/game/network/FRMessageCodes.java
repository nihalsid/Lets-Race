package com.letsrace.game.network;

public class FRMessageCodes {
	public static final byte SELECTED_CAR_0 = (byte) 0x80;
	public static final byte SELECTED_CAR_1 = (byte) 0x81;
	public static final byte SELECTED_CAR_2 = (byte) 0x82;
	public static final byte SELECTED_CAR_3 = (byte) 0x83;
	public static final byte PROCEED_TO_GAME_SCREEN = (byte) 0x08;
	public static final byte PING_DETECT_REQ = (byte)0x78;
	public static final byte PING_DETECT_RES = (byte)0xF8;
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
	
	public static byte extractHeaderExtField(byte msg){
		return (byte) (msg&0x07);
	}
	public static byte convertToCorrespondingPlayerMessageCode(byte msg, int playerNumber){
		return (byte) ((msg&0x7F)|(playerNumber&0x07));
	}
	public static String nameFromHeader(byte b){
		switch(b){
		case SELECTED_CAR_0: return "SELECTED_CAR_0";
		case  SELECTED_CAR_1: return "SELECTED_CAR_1";
		case  SELECTED_CAR_2: return "SELECTED_CAR_2";
		case  SELECTED_CAR_3: return "SELECTED_CAR_3";
		case  PROCEED_TO_GAME_SCREEN: return "PROCEED_TO_GAME_SCREEN";
		case  PING_DETECT_REQ: return"PING_DETECT_REQ";
		case  PING_DETECT_RES: return"PING_DETECT_RES";
		case  CAR_PICK_CONFIRMED_PLAYER_0: return "CAR_PICK_CONFIRMED_PLAYER_0";
		case  CAR_PICK_CONFIRMED_PLAYER_1: return "CAR_PICK_CONFIRMED_PLAYER_1";
		case  CAR_PICK_CONFIRMED_PLAYER_2: return "CAR_PICK_CONFIRMED_PLAYER_2";
		case  CAR_PICK_CONFIRMED_PLAYER_3: return "CAR_PICK_CONFIRMED_PLAYER_3";
		case  REPICK_CAR: return "REPICK_CAR";
		case  RESYNC_HEAD: return "RESYNC_HEAD";
		case  ACCEL_DOWN: return "ACCEL_DOWN";
		case  ACCEL_DOWN_PLAYER_0: return "ACCEL_DOWN_PLAYER_0";
		case  ACCEL_DOWN_PLAYER_1: return "ACCEL_DOWN_PLAYER_1";
		case  ACCEL_DOWN_PLAYER_2: return "ACCEL_DOWN_PLAYER_2";
		case  ACCEL_DOWN_PLAYER_3: return "ACCEL_DOWN_PLAYER_3";
		case  ACCEL_UP: return "ACCEL_UP";
		case  ACCEL_UP_PLAYER_0: return "ACCEL_UP_PLAYER_0";
		case  ACCEL_UP_PLAYER_1: return "ACCEL_UP_PLAYER_1";
		case  ACCEL_UP_PLAYER_2: return "ACCEL_UP_PLAYER_2";
		case  ACCEL_UP_PLAYER_3: return "ACCEL_UP_PLAYER_3";
		case  TURN_LEFT: return "TURN_LEFT";
		case  TURN_LEFT_PLAYER_0: return "TURN_LEFT_PLAYER_0";
		case  TURN_LEFT_PLAYER_1: return "TURN_LEFT_PLAYER_1";
		case  TURN_LEFT_PLAYER_2: return "TURN_LEFT_PLAYER_2";
		case  TURN_LEFT_PLAYER_3: return "TURN_LEFT_PLAYER_3";
		case  TURN_RIGHT: return "TURN_RIGHT";
		case  TURN_RIGHT_PLAYER_0: return "TURN_RIGHT_PLAYER_0";
		case  TURN_RIGHT_PLAYER_1: return "TURN_RIGHT_PLAYER_1";
		case  TURN_RIGHT_PLAYER_2: return "TURN_RIGHT_PLAYER_2";
		case  TURN_RIGHT_PLAYER_3: return "TURN_RIGHT_PLAYER_3";
		case  STEER_STRAIGHT: return "STEER_STRAIGHT";
		case  STEER_STRAIGHT_PLAYER_0: return "STEER_STRAIGHT_PLAYER_0";
		case  STEER_STRAIGHT_PLAYER_1: return "STEER_STRAIGHT_PLAYER_1";
		case  STEER_STRAIGHT_PLAYER_2: return "STEER_STRAIGHT_PLAYER_2";
		case  STEER_STRAIGHT_PLAYER_3: return "STEER_STRAIGHT_PLAYER_3";
		case  NO_ACCELERATE: return "NO_ACCELERATE";
		case  NO_ACCELERATE_PLAYER_0: return "NO_ACCELERATE_PLAYER_0";
		case  NO_ACCELERATE_PLAYER_1: return "NO_ACCELERATE_PLAYER_1";
		case  NO_ACCELERATE_PLAYER_2: return "NO_ACCELERATE_PLAYER_2";
		case  NO_ACCELERATE_PLAYER_3: return "NO_ACCELERATE_PLAYER_3";
		}
		return null;
	}
}
