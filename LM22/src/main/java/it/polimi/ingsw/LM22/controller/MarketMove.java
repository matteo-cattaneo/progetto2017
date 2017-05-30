package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;

public class MarketMove extends MemberMove {
	private Integer marketSpaceSelected;

	public MarketMove(Player p, FamilyMember memberUsed, Resource servantsAdded, Integer marketSpaceSelected) {
		super(p, memberUsed, servantsAdded);
		this.marketSpaceSelected = marketSpaceSelected;
	}

	public Integer getMarketSpaceSelected() {
		return marketSpaceSelected;
	}
}
