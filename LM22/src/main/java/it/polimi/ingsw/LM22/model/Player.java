package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.List;

import it.polimi.ingsw.LM22.model.excommunication.ExCommunication;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class Player implements Serializable {
	private final Integer NUM_LEADER_CARDS = 4;
	private final String nickname;
	private final String color;
	private final List<FamilyMember> members;
	private final PersonalBoard personalBoard = new PersonalBoard();
	private List<ExCommunication> exCommunications;
	private List<LeaderCard> leaderCards;
	private List<Effect> effects;

	public Player(String nickname, String color, List<FamilyMember> members) {
		super();
		this.nickname = nickname;
		this.color = color;
		this.members = members;
	}

	public String getNickname() {
		return nickname;
	}

	public void setExCommunications(List<ExCommunication> exCommunications) {
		this.exCommunications = exCommunications;
	}

	public void setLeaderCards(List<LeaderCard> leaderCards) {
		this.leaderCards = leaderCards;
	}

	public void setEffects(List<Effect> effects) {
		this.effects = effects;
	}

	public String getColor() {
		return color;
	}

	public List<FamilyMember> getMembers() {
		return members;
	}

	public PersonalBoard getPersonalBoard() {
		return personalBoard;
	}

	public List<ExCommunication> getExCommunications() {
		return exCommunications;
	}

	public List<LeaderCard> getLeaderCards() {
		return leaderCards;
	}

	public List<Effect> getEffects() {
		return effects;
	}

}
