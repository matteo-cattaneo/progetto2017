package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class CardRequest extends LeaderCardRequest implements Serializable{

	private static final long serialVersionUID = 1L;
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
