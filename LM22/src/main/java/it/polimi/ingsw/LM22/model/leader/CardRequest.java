package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class CardRequest extends LeaderCardRequest implements Serializable{
	
	private Integer territoryCards; 
	private Integer characterCards;
	private Integer buildingCards;
	private Integer ventureCards;
	
	public Integer getTerritoryCards() {
		return territoryCards;
	}
	public Integer getCharacterCards() {
		return characterCards;
	}
	public Integer getBuildingCards() {
		return buildingCards;
	}
	public Integer getVentureCards() {
		return ventureCards;
	}
	
	
	
}
