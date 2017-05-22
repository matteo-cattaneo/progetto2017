package it.polimi.ingsw.LM22.model;

public class FamilyMember {
	private final Integer UNCOLORED_PAWN_VALUE = 0;
	private final Player player;
	private final String color;
	private Integer value;
	private boolean used;
	private boolean uncolored;
	
	public FamilyMember (Player player, String color){
		this.player = player;
		this.color = color;
		used = false;
	}
	
	public Integer getValue() {
		if (!uncolored)
			return value;
		return UNCOLORED_PAWN_VALUE;
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
