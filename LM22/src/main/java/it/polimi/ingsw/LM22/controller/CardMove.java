package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;

public class CardMove extends MemberMove {
	private Integer towerSelected;
	private Integer levelSelected;

	public CardMove(Player p, FamilyMember memberUsed, Resource servantsAdded, Integer towerSelected,
			Integer levelSelected) {
		super(p, memberUsed, servantsAdded);
		this.towerSelected = towerSelected;
		this.levelSelected = levelSelected;
	}

	public Integer getTowerSelected() {
		return towerSelected;
	}

	public Integer getLevelSelected() {
		return levelSelected;
	}
}
