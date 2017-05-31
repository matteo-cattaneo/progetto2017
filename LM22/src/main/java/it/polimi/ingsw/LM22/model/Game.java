package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	private final Integer NUM_BONUS_TILE = 4;
	private BoardGame boardgame;
	private Integer period;
	private Integer round;
	private Player[] players;
	private List<Player> playersOrder;
	private Collection<TerritoryCard> territoryCards;
	private Collection<CharacterCard> characterCards;
	private Collection<BuildingCard> buildingCards;
	private Collection<VentureCard> ventureCards;
	private Collection<LeaderCard> leaderCards;
	private PersonalBonusTile personalBonusTile[];

	/*
	 * durante la creazione della Game dovrò inizializzare tutti gli attributi
	 * messi a final (saranno sempre quelli)
	 */
	public BoardGame getBoardgame() {
		return boardgame;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public void setTerritoryCards(Collection<TerritoryCard> territoryCards) {
		this.territoryCards = territoryCards;
	}

	public void setCharacterCards(Collection<CharacterCard> characterCards) {
		this.characterCards = characterCards;
	}

	public void setBuildingCards(Collection<BuildingCard> buildingCards) {
		this.buildingCards = buildingCards;
	}

	public void setVentureCards(Collection<VentureCard> ventureCards) {
		this.ventureCards = ventureCards;
	}

	public void setLeaderCards(Collection<LeaderCard> leaderCards) {
		this.leaderCards = leaderCards;
	}

	public Player[] getPlayers() {
		return players;
	}

	public List<Player> getPlayersOrder() {
		return playersOrder;
	}

	public void setPlayersOrder(List<Player> playersOrder) {
		this.playersOrder = playersOrder;
	}

	public Collection<TerritoryCard> getTerritoryCards() {
		return territoryCards;
	}

	public Collection<CharacterCard> getCharacterCards() {
		return characterCards;
	}

	public Collection<BuildingCard> getBuildingCards() {
		return buildingCards;
	}

	public Collection<VentureCard> getVentureCards() {
		return ventureCards;
	}

	public Collection<LeaderCard> getLeaderCards() {
		return leaderCards;
	}

	public PersonalBonusTile[] getPersonalBonusTile() {
		return personalBonusTile;
	}

	public Integer getNUM_BONUS_TILE() {
		return NUM_BONUS_TILE;
	}

}
