package it.polimi.ingsw.LM22.model;

import java.util.List;

public class Player {
	private final Integer NUM_LEADER_CARDS = 4;
	private String nickname;
	private String color;
	private List<FamilyMember> members;
	private PersonalBoard personalBoard;
	private List<ExInterface> exCommunications;
	private LeaderCard leaderCards[];
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public List<FamilyMember> getMembers() {
		return members;
	}
	
	public void setMembers(List<FamilyMember> members) {
		this.members = members;
	}
	
	public PersonalBoard getPersonalBoard() {
		return personalBoard;
	}
	
	public void setPersonalBoard(PersonalBoard personalBoard) {
		this.personalBoard = personalBoard;
	}
	
	public List<ExInterface> getExCommunications() {
		return exCommunications;
	}
	
	public void setExCommunications(List<ExInterface> exCommunications) {
		this.exCommunications = exCommunications;
	}
	
	public LeaderCard[] getLeaderCards() {
		return leaderCards;
	}
	
	public void setLeaderCards(LeaderCard[] leaderCards) {
		this.leaderCards = leaderCards;
	}
	
}
