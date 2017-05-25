package it.polimi.ingsw.LM22.model;

import java.util.HashMap;
import java.util.List;

public class Tower {
	
	private final Integer LEVELS = 4;
	private final String towerType;	
	private HashMap<CardSpace, DevelopmentCard> cards;
	private boolean occupied;
	private List<FamilyMember> coloredMembersOnIt;
	
	public Tower(String towerType) {
		this.towerType = towerType;
		occupied = false;
	}
	
	public HashMap<CardSpace, DevelopmentCard> getCards() {
		return cards;
	}

	public void setCards(HashMap<CardSpace, DevelopmentCard> cards) {
		this.cards = cards;
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
