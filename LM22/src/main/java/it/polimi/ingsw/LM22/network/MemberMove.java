package it.polimi.ingsw.LM22.network;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Resource;

public abstract class MemberMove extends AbstractMove {
	private FamilyMember memberUsed;
	private Resource servantsAdded;

	public FamilyMember getMemberUsed() {
		return memberUsed;
	}

	public Resource getServantsAdded() {
		return servantsAdded;
	}

}
