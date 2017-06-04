package it.polimi.ingsw.LM22.model;

import java.io.Serializable;
import java.util.List;

public class CouncilSpace extends AbstractSpace implements Serializable {

	private static final long serialVersionUID = 923010337475400979L;
	private final Integer councilPrivilege = 1;
	private final Resource reward;
	private List<FamilyMember> members;
		
	
	/*
	 * ATTENZIONE con i costruttori perchè reward va caricato da file
	 * e probabilmente non servono 
	 * */
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
