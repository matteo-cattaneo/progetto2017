package it.polimi.ingsw.LM22.model;

import java.util.List;



public class Game {
	private final Integer NUM_BONUS_TILE = 4;
	private BoardGame boardgame;
	private Integer period;
	private Integer round;
	private Player[] players;
	private List<Player> playersOrder;
	private List<DevelopmentCard> developmentCards;
	private List<LeaderCard> leaderCards;
	private PersonalBonusTile personalBonusTile[];
	public BoardGame getBoardgame() {
		return boardgame;
	}
	public void setBoardgame(BoardGame boardgame) {
		this.boardgame = boardgame;
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
	public void setPlayers(Player[] players) {
		this.players = players;
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
	public void setDevelopmentCards(List<DevelopmentCard> developmentCards) {
		this.developmentCards = developmentCards;
	}
	public List<LeaderCard> getLeaderCards() {
		return leaderCards;
	}
	public void setLeaderCards(List<LeaderCard> leaderCards) {
		this.leaderCards = leaderCards;
	}
	public PersonalBonusTile[] getPersonalBonusTile() {
		return personalBonusTile;
	}
	public void setPersonalBonusTile(PersonalBonusTile[] personalBonusTile) {
		this.personalBonusTile = personalBonusTile;
	}
	public Integer getNUM_BONUS_TILE() {
		return NUM_BONUS_TILE;
	}

}
