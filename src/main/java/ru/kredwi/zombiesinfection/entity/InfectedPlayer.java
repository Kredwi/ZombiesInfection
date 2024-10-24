package ru.kredwi.zombiesinfection.entity;

public class InfectedPlayer {
	private final long unix;
	private final int infectTime;
	
	public InfectedPlayer(long unix, int infectTime) {
		this.unix = unix;
		this.infectTime = infectTime;
	}
	
	public long getUNIX() { return unix; }
	public int getInfectTime() { return infectTime; }
}
