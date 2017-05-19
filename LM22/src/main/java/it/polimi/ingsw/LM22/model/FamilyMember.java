package it.polimi.ingsw.LM22.model;

public class FamilyMember {
	private final Integer UNCOLORED_PAWN_VALUE = 0;
	private final Player player; 	//da inizializzare con il costruttore
	private final String color;		//da inizializzare con il costruttore
	private Integer value;
	private boolean used;
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public Player getPlayer() {
		return player;
	}
	public String getColor() {
		return color;
	}
	
}
