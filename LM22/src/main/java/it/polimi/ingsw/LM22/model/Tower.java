package it.polimi.ingsw.LM22.model;

import java.util.List;

public class Tower {
	
	private final Integer LEVELS = 4;
	private final String towerType;	
	private Floor floor[];
	private boolean occupied;
	private List<FamilyMember> coloredMembersOnIt;
	
	public Tower(String towerType) {
		this.towerType = towerType;
		occupied = false;
	}

	public Floor[] getFloor() {
		return floor;
	}

	public void setFloor(Floor[] floor) {
		this.floor = floor;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	public List<FamilyMember> getColoredMembersOnIt() {
		return coloredMembersOnIt;
	}
	
	public void setColoredMembersOnIt(List<FamilyMember> coloredMembersOnIt) {
		this.coloredMembersOnIt = coloredMembersOnIt;
	}
	
	public String getTowerType() {
		return towerType;
	}
	
}
