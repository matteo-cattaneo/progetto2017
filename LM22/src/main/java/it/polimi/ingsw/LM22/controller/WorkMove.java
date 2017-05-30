package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;

public class WorkMove extends MemberMove {
	private String workType;

	public WorkMove(Player p, FamilyMember memberUsed, Resource servantsAdded, String workType) {
		super(p, memberUsed, servantsAdded);
		this.workType = workType;
	}

	public String getWorkType() {
		return workType;
	}
}
