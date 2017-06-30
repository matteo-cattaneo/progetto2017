package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class ResourceCardRequest extends LeaderCardRequest implements Serializable {

	private static final long serialVersionUID = 1616137976175717083L;
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

	@Override
	public String getInfo() {
		String info;
		info = "You must have%n";
		if (!getTerritoryCards().equals(0))
			info = info + "- " + getTerritoryCards() + " Territory cards%n";
		if (!getCharacterCards().equals(0))
			info = info + "- " + getCharacterCards() + " Character cards%n";
		if (!getBuildingCards().equals(0))
			info = info + "- " + getBuildingCards() + " Buildings cards%n";
		if (!getVentureCards().equals(0))
			info = info + "- " + getVentureCards() + " Venture cards%n";
		if (!"No resource%n".equals(getResource().getInfo()))
			info = info + "- " + getResource().getInfo();

		return info;
	}

}
