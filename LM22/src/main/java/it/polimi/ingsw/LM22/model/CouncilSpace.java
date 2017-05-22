package it.polimi.ingsw.LM22.model;

import java.util.List;

public class CouncilSpace extends AbstractSpace{

	private final Integer councilPrivilege = 1;
	private final Resource reward;
	private List<FamilyMember> members;
		
	public CouncilSpace (Integer requirement, Resource resource){
		super(requirement);
		this.reward = resource;
	}
		
	public List<FamilyMember> getMembers() {
		return members;
	}
	
	public void setMembers(List<FamilyMember> members) {
		this.members = members;
	}
	
	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
	
	public Resource getReward() {
		return reward;
	}
	
		
}
