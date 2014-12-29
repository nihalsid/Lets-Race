package com.letsrace.game.network;

public interface FRMessageCodes {
	byte SELECTED_CAR_0 = (byte) 0x80;
	byte SELECTED_CAR_1 = (byte) 0x81;
	byte SELECTED_CAR_2 = (byte) 0x82;
	byte SELECTED_CAR_3 = (byte) 0x83;
	byte PROCEED_TO_GAME_SCREEN = (byte) 0x08;
	byte CAR_PICK_CONFIRMED = (byte) 0x10;
	byte ACK_PICK_CONFIRMED = (byte) 0x90;
	byte REPICK_CAR = (byte) 0x18;
	byte RESYNC_HEAD = (byte) 0x20;
	byte ACCEL_DOWN = (byte) 0xA8;
	byte ACCEL_DOWN_PLAYER_0 = (byte) 0x28;
	byte ACCEL_DOWN_PLAYER_1 = (byte) 0x29;
	byte ACCEL_DOWN_PLAYER_2 = (byte) 0x2A;
	byte ACCEL_DOWN_PLAYER_3 = (byte) 0x2B;
	byte ACCEL_UP = (byte) 0xB0;
	byte ACCEL_UP_PLAYER_0 = (byte) 0x30;
	byte ACCEL_UP_PLAYER_1 = (byte) 0x31;
	byte ACCEL_UP_PLAYER_2 = (byte) 0x32;
	byte ACCEL_UP_PLAYER_3 = (byte) 0x33;
	byte TURN_LEFT = (byte) 0xB8;
	byte TURN_LEFT_PLAYER_0 = (byte) 0x38;
	byte TURN_LEFT_PLAYER_1 = (byte) 0x39;
	byte TURN_LEFT_PLAYER_2 = (byte) 0x3A;
	byte TURN_LEFT_PLAYER_3 = (byte) 0x3B;
	byte TURN_RIGHT = (byte) 0x40;
	byte TURN_RIGHT_PLAYER_0 = (byte) 0xC0;
	byte TURN_RIGHT_PLAYER_1 = (byte) 0xC1;
	byte TURN_RIGHT_PLAYER_2 = (byte) 0xC2;
	byte TURN_RIGHT_PLAYER_3 = (byte) 0xC3;
}
