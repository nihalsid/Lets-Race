package com.letsrace.game.network;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;

import com.letsrace.game.GameWorld;
import com.letsrace.game.Player;
import com.letsrace.game.car.Car;

public class ServerUtils {
	public static boolean isCarSelected(GameWorld world, int number) {
		Set keySet = world.players.keySet();
		for (Object key : keySet) {
			Player player = world.players.get(key);
			if (player.playerType == number) {
				return true;
			}
		}
		return false;
	}
	
	public static void updateCars(GameWorld world, float delta){
		Set keySet = world.players.keySet();
		for (Object key : keySet) {
			Car car = world.players.get(key).car;
			car.update(delta);
		}
	}

	public static byte[] generateCarSyncMessage(GameWorld gameWorld,
			ArrayList<String> participantIds) {
		int ctr = 0;
		byte playerNumber = 0;
		byte[] message = new byte[1 + participantIds.size() * 21];
		message[ctr++] = FRMessageCodes.SYNC_CARS;

		for (String participantId : participantIds) {
			Car c = gameWorld.players.get(participantId).car;
			message[ctr++] = playerNumber++;
			byte[] posX = ByteBuffer.allocate(4)
					.putFloat(c.getWorldPosition().x).array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = posX[i];
			byte[] posY = ByteBuffer.allocate(4)
					.putFloat(c.getWorldPosition().y).array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = posY[i];
			byte[] wAngle = ByteBuffer.allocate(4).putFloat(c.wheelAngle)
					.array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = wAngle[i];
			byte[] cBodyAngle = ByteBuffer.allocate(4)
					.putFloat(c.getBodyAngle()).array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = cBodyAngle[i];
			byte[] cSpeed = ByteBuffer.allocate(4).putFloat(c.getSpeedKMH())
					.array();
			for (int i = 0; i < 4; i++)
				message[ctr++] = cSpeed[i];
		}
		return message;

	}
}
