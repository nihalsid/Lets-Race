package com.letsrace.game.android;

import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.letsrace.game.Message;
import com.letsrace.game.network.FRMessageListener;
import com.letsrace.game.network.FRNetwork;

public class FRMessageHandler implements RealTimeMessageReceivedListener {
	byte TO_SERVER = (byte) 0x80;
	byte TO_CLIENT = (byte) 0x00;
	FRMessageListener serverListener;
	FRMessageListener clientListener;

	FRNetwork network;
	
	public FRMessageHandler(FRNetwork network) {
		this.network = network;
	}

	public void setServerMessageListener(FRMessageListener listener) {
		serverListener = listener;
	}

	public void setClientMessageListener(FRMessageListener listener) {
		clientListener = listener;
	}

	@Override
	public void onRealTimeMessageReceived(RealTimeMessage rtm) {
		byte[] buf = rtm.getMessageData();
		if ((buf[0] & TO_SERVER) == TO_SERVER) {
			network.addToServerQueue(new Message(buf, rtm.getSenderParticipantId()));
			serverListener.onMessageRecieved(buf, rtm.getSenderParticipantId());
		} else {
			network.addToClientQueue(new Message(buf, rtm.getSenderParticipantId()));
			clientListener.onMessageRecieved(buf, rtm.getSenderParticipantId());
		}
	}
}
