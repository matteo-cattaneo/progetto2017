package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonalBoard implements Serializable {
	
	private static final long serialVersionUID = -7460184029407285498L;
	private PersonalBonusTile bonusBoard;
	private Resource resources;
	private List<TerritoryCard> territoriesCards = new ArrayList<>();
	private List<CharacterCard> charactersCards = new ArrayList<>();
	private List<BuildingCard> buildingsCards = new ArrayList<>();
	private List<VentureCard> venturesCards = new ArrayList<>();

	public Resource getResources() {
		return resources;
	}

	public void setResources(Resource resources) {
		this.resources = resources;
	}

	public PersonalBonusTile getBonusBoard() {
		return bonusBoard;
	}

	public void setBonusBoard(PersonalBonusTile bonusBoard) {
		this.bonusBoard = bonusBoard;
	}

	public List<TerritoryCard> getTerritoriesCards() {
		return territoriesCards;
	}

	public void setTerritoriesCards(List<TerritoryCard> territoriesCards) {
		this.territoriesCards = territoriesCards;
	}

	public List<CharacterCard> getCharactersCards() {
		return charactersCards;
	}

	public void setCharactersCards(List<CharacterCard> charactersCards) {
		this.charactersCards = charactersCards;
	}

	public List<BuildingCard> getBuildingsCards() {
		return buildingsCards;
	}

	public void setBuildingsCards(List<BuildingCard> buildingsCards) {
		this.buildingsCards = buildingsCards;
	}

	public List<VentureCard> getVenturesCards() {
		return venturesCards;
	}

	public void setVenturesCards(List<VentureCard> venturesCards) {
		this.venturesCards = venturesCards;
	}

}
