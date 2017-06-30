package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.List;

import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class Game implements Serializable {

	private static final long serialVersionUID = -4098227091469224262L;
	private final BoardGame boardgame = new BoardGame();
	private Integer period;
	private Integer round;
	private Player[] players;
	private List<Player> playersOrder;
	private transient List<TerritoryCard> territoryCards;
	private transient List<CharacterCard> characterCards;
	private transient List<BuildingCard> buildingCards;
	private transient List<VentureCard> ventureCards;
	private transient List<LeaderCard> leaderCards;
	private PersonalBonusTile personalBonusTile[];
	private long moveTimer; // caricabile da file (secondi)

	public long getMoveTimer() {
		return moveTimer;
	}

	public void setMoveTimer(long moveTimer) {
		this.moveTimer = moveTimer;
	}

	public void setPersonalBonusTile(PersonalBonusTile[] personalBonusTile) {
		this.personalBonusTile = personalBonusTile;
	}

	public BoardGame getBoardgame() {
		return boardgame;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
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

	public void setTerritoryCards(List<TerritoryCard> territoryCards) {
		this.territoryCards = territoryCards;
	}

	public void setCharacterCards(List<CharacterCard> characterCards) {
		this.characterCards = characterCards;
	}

	public void setBuildingCards(List<BuildingCard> buildingCards) {
		this.buildingCards = buildingCards;
	}

	public void setVentureCards(List<VentureCard> ventureCards) {
		this.ventureCards = ventureCards;
	}

	public void setLeaderCards(List<LeaderCard> leaderCards) {
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

	public List<TerritoryCard> getTerritoryCards() {
		return territoryCards;
	}

	public List<CharacterCard> getCharacterCards() {
		return characterCards;
	}

	public List<BuildingCard> getBuildingCards() {
		return buildingCards;
	}

	public List<VentureCard> getVentureCards() {
		return ventureCards;
	}

	public List<LeaderCard> getLeaderCards() {
		return leaderCards;
	}

	public PersonalBonusTile[] getPersonalBonusTile() {
		return personalBonusTile;
	}

}
