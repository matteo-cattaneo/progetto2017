package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class FamilyMember  implements Serializable {

	private static final long serialVersionUID = -4684293272659060314L;
	private final Integer UNCOLORED_PAWN_VALUE = 0;
	private final Player player;
	private final String color; //"UNCOLORED" means that is uncolored
	private Integer value;
	private Boolean used;
	
	public FamilyMember (Player player, String color){
		this.player = player;
		this.color = color;
		used = false;
	}
	
	public Integer getValue() {
		if (color!="UNCOLORED")
			return value;
		return UNCOLORED_PAWN_VALUE;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public Boolean isUsed() {
		return used;
	}
	
	public void setUsed(Boolean used) {
		this.used = used;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getColor() {
		return color;
	}
	
}
