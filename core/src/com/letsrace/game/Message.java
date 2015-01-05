package com.letsrace.game;

public class Message {
	public byte msg[];
	public String id;

	public Message(byte[] msg, String id) {
		this.msg = msg;
		this.id = id;
	}
}
