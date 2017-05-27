package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Resource;

public class WorkMove extends MemberMove {
	private String workType;

	public WorkMove(FamilyMember memberUsed, Resource servantsAdded, String workType) {
		super(memberUsed, servantsAdded);
		this.workType = workType;
	}

	public String getWorkType() {
		return workType;
	}
}
