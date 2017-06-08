package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class Player implements Serializable {

	private static final long serialVersionUID = 7778308561624051543L;
	private final String nickname;
	private final String color;
	private List<FamilyMember> members;
	private final PersonalBoard personalBoard = new PersonalBoard();
	private List<LeaderCard> leaderCards = new ArrayList<LeaderCard>();
	private List<LeaderCard> activatedLeaderCards = new ArrayList<LeaderCard>();;
	private List<Effect> effects = new ArrayList<Effect>();

	public Player(String nickname, String color) {
		this.nickname = nickname;
		this.color = color;
	}

	public void setMembers(List<FamilyMember> members) {
		this.members = members;
	}

	public String getNickname() {
		return nickname;
	}

	public void setLeaderCards(List<LeaderCard> leaderCards) {
		this.leaderCards = leaderCards;
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

	public List<LeaderCard> getActivatedLeaderCards() {
		return activatedLeaderCards;
	}

	public List<LeaderCard> getLeaderCards() {
		return leaderCards;
	}

	public List<Effect> getEffects() {
		return effects;
	}

}
