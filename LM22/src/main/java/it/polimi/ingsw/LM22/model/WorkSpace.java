package it.polimi.ingsw.LM22.model;

import java.util.List;

public class WorkSpace {
	//costante da inizializzare con il costruttore
	//che andrò a richiamare il costruttore super();
	private final String workType; 
	private final Integer MALUS = 3;
	private List<FamilyMember> members;
		
		
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
