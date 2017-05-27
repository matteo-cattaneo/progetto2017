package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Resource;

public abstract class MemberMove extends AbstractMove {
	private FamilyMember memberUsed;
	private Resource servantsAdded;	

	public MemberMove(FamilyMember memberUsed, Resource servantsAdded) {
		this.memberUsed = memberUsed;
		this.servantsAdded = servantsAdded;
	}

	public FamilyMember getMemberUsed() {
		return memberUsed;
	}

	public Resource getServantsAdded() {
		return servantsAdded;
	}

}
