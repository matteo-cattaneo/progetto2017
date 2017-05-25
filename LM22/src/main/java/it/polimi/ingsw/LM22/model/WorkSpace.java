package it.polimi.ingsw.LM22.model;

import java.util.List;

public class WorkSpace extends AbstractSpace{
	
	private final String workType; 
	private final Integer MALUS = 3;
	private List<FamilyMember> members;

	public WorkSpace(Integer requirement, String workType){
		super(requirement);
		this.workType = workType;
	}		
		
	public List<FamilyMember> getMembers() {
		return members;
	}
	public void setMembers(List<FamilyMember> members) {
		this.members = members;
	}
	public String getWorkType() {
		return workType;
	}
		
}
