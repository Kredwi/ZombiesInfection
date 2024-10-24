package ru.kredwi.zombiesinfection.utils;

import java.util.concurrent.TimeUnit;

import ru.kredwi.zombiesinfection.entity.InfectedPlayer;
import ru.kredwi.zombiesinfection.files.ZIConfig;

public class InfectionUtils {
	
	public int getInfectedRandomTime() {
		int rangeTo = ZIConfig.RANDOM_TIME_FROM_RANGE_TO.asInt();
		int rangeFrom = ZIConfig.RANDOM_TIME_FROM_RANGE_FROM.asInt();
		
		int currentRandom = (int) (Math.random() * (rangeTo - rangeFrom + 1)) + rangeFrom;
		return ZIConfig.RANDOM_TIME_FROM_RANGE_ENABLE.asBoolean() ? currentRandom : ZIConfig.EFFECT_WILL_APPLIED_AFTER.asInt();
	}
	
	public boolean timeIsOver(InfectedPlayer player) {
		return this.getLeaveTime(player) > TimeUnit.MINUTES.toSeconds(player.getInfectTime());
	}
	
	public long getLeaveTime(InfectedPlayer player) {
		return (System.currentTimeMillis() / 1000L) - player.getUNIX();
	}
	
}
