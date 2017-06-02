package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.List;

import it.polimi.ingsw.LM22.model.excommunication.ExCommunication;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class Player implements Serializable {
	private final Integer NUM_LEADER_CARDS = 4;
	private String nickname;
	private String color;
	private List<FamilyMember> members;
	private final PersonalBoard personalBoard = new PersonalBoard();
	private List<ExCommunication> exCommunications;
	private List<LeaderCard> leaderCards;
	private List<Effect> effects;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setMembers(List<FamilyMember> members) {
		this.members = members;
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
