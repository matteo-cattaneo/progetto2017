package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class ResourceCardRequest extends LeaderCardRequest implements Serializable{

	private Resource resource;
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
	public Resource getResource() {
		return resource;
	}
	
	
}
