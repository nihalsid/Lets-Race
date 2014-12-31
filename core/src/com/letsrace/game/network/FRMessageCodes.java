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
	public static final byte TURN_RIGHT = (byte) 0x40;
	public static final byte TURN_RIGHT_PLAYER_0 = (byte) 0xC0;
	public static final byte TURN_RIGHT_PLAYER_1 = (byte) 0xC1;
	public static final byte TURN_RIGHT_PLAYER_2 = (byte) 0xC2;
	public static final byte TURN_RIGHT_PLAYER_3 = (byte) 0xC3;
	public static final byte STEER_STRAIGHT = (byte) 0x48;
	public static final byte STEER_STRAIGHT_PLAYER_0 = (byte) 0xD0;
	public static final byte STEER_STRAIGHT_PLAYER_1 = (byte) 0xD1;
	public static final byte STEER_STRAIGHT_PLAYER_2 = (byte) 0xD2;
	public static final byte STEER_STRAIGHT_PLAYER_3 = (byte) 0xD3;
	
	public static byte extractHeaderExtField(byte msg){
		return (byte) (msg&0x07);
	}
	public static byte convertToCorrespondingPlayerMessageCode(byte msg, int playerNumber){
		return (byte) ((msg|0x80)|(playerNumber&0x07));
	}
}
