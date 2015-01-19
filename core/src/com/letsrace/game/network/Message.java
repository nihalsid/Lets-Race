package com.letsrace.game.network;

public class Message {
	public byte msg[];
	public String id;

	public Message(byte[] msg, String id) {
		this.msg = msg;
		this.id = id;
	}

	public byte[] getMessageData() {
		return msg;
	}

	public String getSenderParticipantId() {
		return id;
	}
	
	@Override
	public String toString() {
		return FRMessageCodes.nameFromHeader(msg[0])+'['+msg[1]+']';
	}
}