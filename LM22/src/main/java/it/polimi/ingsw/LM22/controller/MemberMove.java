package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;

public abstract class MemberMove extends AbstractMove {
	private FamilyMember memberUsed;
	private Resource servantsAdded;	

	public MemberMove(Player p, FamilyMember memberUsed, Resource servantsAdded) {
		super(p);
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
