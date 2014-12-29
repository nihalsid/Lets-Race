package com.letsrace.game.android;

import java.util.Arrays;

import android.util.Log;

import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.letsrace.game.network.FRMessageListener;

public class FRMessageHandler implements RealTimeMessageReceivedListener {
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

	@Override
	public void onRealTimeMessageReceived(RealTimeMessage rtm) {
		byte[] buf = rtm.getMessageData();
		Log.d(AndroidLauncher.TAG, "Message received: " + Arrays.toString(buf));
		if ((buf[0] & TO_SERVER) == TO_SERVER)
			serverListener.onMessageRecieved(buf, rtm.getSenderParticipantId());
		else
			clientListener.onMessageRecieved(buf, rtm.getSenderParticipantId());
	}

}
