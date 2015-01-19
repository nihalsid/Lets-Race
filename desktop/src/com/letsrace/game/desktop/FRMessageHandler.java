package com.letsrace.game.desktop;

import com.letsrace.game.network.FRMessageListener;
import com.letsrace.game.network.Message;

public class FRMessageHandler {
	byte TO_SERVER = (byte) 0x80;
	byte TO_CLIENT = (byte) 0x00;
	FRMessageListener serverListener;
	FRMessageListener clientListener;

	public FRMessageHandler() {
	}

	public void setServerMessageListener(FRMessageListener listener) {
		serverListener = listener;
	}

	public void setClientMessageListener(FRMessageListener listener) {
		clientListener = listener;
	}

	public void onRealTimeMessageReceived(Message rtm) {
		byte[] buf = rtm.getMessageData();
		if ((buf[0] & TO_SERVER) == TO_SERVER) {
			serverListener.onMessageRecieved(buf, rtm.getSenderParticipantId());
		} else {
			clientListener.onMessageRecieved(buf, rtm.getSenderParticipantId());
		}
	}

}
