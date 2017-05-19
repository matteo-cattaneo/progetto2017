package it.polimi.ingsw.LM22.model;

import java.util.List;

public class CouncilSpace extends AbstractSpace{
	//costante da inizializzare con il costruttore
	//che andrò a richiamare il costruttore super();
	private final Integer councilPrivilege;
	private final Resource reward;
	private List<FamilyMember> members;
		
		
		
	public List<FamilyMember> getMembers() {
		return members;
	}
	public void setMembers(List<FamilyMember> members) {
		this.members = members;
	}
	public CouncilPrivilege getCouncilPrivilege() {
		return councilPrivilege;
	}
	public void setCouncilPrivilege(CouncilPrivilege councilPrivilege) {
		this.councilPrivilege = councilPrivilege;
	}
	public Resource getReward() {
		return reward;
	}
	public void setReward(Resource reward) {
		this.reward = reward;
	}
		
}
