package it.polimi.ingsw.LM22.network;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Resource;

public class MarketMove extends MemberMove {
	private Integer marketSpaceSelected;

	public MarketMove(FamilyMember memberUsed, Resource servantsAdded, Integer marketSpaceSelected) {
		super(memberUsed, servantsAdded);
		this.marketSpaceSelected = marketSpaceSelected;
	}

	public Integer getMarketSpaceSelected() {
		return marketSpaceSelected;
	}
}
