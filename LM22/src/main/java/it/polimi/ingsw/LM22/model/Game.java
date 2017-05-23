package it.polimi.ingsw.LM22.model;

import java.util.List;

public class Game {
	private final Integer NUM_BONUS_TILE = 4;
	private final BoardGame boardgame;
	private Integer period;
	private Integer round;
	private final Player[] players;
	private List<Player> playersOrder;
	private final List<DevelopmentCard> developmentCards;
	private final List<AbstractLeaderCard> leaderCards;
	private final PersonalBonusTile personalBonusTile[];

	
	/*durante la creazione della Game dovrò inizializzare tutti 
	 * gli attributi messi a final (saranno sempre quelli)*/
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

	public Player[] getPlayers() {
		return players;
	}

	public List<Player> getPlayersOrder() {
		return playersOrder;
	}

	public void setPlayersOrder(List<Player> playersOrder) {
		this.playersOrder = playersOrder;
	}

	public List<DevelopmentCard> getDevelopmentCards() {
		return developmentCards;
	}

	public List<AbstractLeaderCard> getLeaderCards() {
		return leaderCards;
	}

	public PersonalBonusTile[] getPersonalBonusTile() {
		return personalBonusTile;
	}

	public Integer getNUM_BONUS_TILE() {
		return NUM_BONUS_TILE;
	}

}
