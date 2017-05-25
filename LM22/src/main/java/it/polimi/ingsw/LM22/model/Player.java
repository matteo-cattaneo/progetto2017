package it.polimi.ingsw.LM22.model;

import java.util.List;

import it.polimi.ingsw.LM22.model.excommunication.ExCommunication;

public class Player {
	private final Integer NUM_LEADER_CARDS = 4;
	private final String nickname;
	private final String color;
	private final List<FamilyMember> members;
	private final PersonalBoard personalBoard;
	private List<ExCommunication> exCommunications;
	private List<LeaderCard> leaderCards;
	
	public String getNickname() {
		return nickname;
	}
	/*tutti i metodi SET per adesso sono commentati per decidere 
	 * poi come verrà inizializzata tutta la partita*/
//	public void setNickname(String nickname) {
//		this.nickname = nickname;
//	}
	
	public String getColor() {
		return color;
	}
	
//	public void setColor(String color) {
//		this.color = color;
//	}
	
	public List<FamilyMember> getMembers() {
		return members;
	}
	
//	public void setMembers(List<FamilyMember> members) {
//		this.members = members;
//	}
	
	public PersonalBoard getPersonalBoard() {
		return personalBoard;
	}
	
//	public void setPersonalBoard(PersonalBoard personalBoard) {
//		this.personalBoard = personalBoard;
//	}
	
	public List<ExCommunication> getExCommunications() {
		return exCommunications;
	}
	
//	public void setExCommunications(List<ExInterface> exCommunications) {
//		this.exCommunications = exCommunications;
//	}

	public List<LeaderCard> getLeaderCards() {
		return leaderCards;
	}

	
	
}
