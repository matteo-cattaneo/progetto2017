package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;

public class CardMove extends MemberMove {
	private Integer towerSelected;
	private Integer levelSelected;
//	private EffectManager effectManager;

	public CardMove(Player p, FamilyMember memberUsed, Resource servantsAdded, Integer towerSelected, Integer levelSelected/*,
			EffectManager effectManager*/) {
		super(p, memberUsed, servantsAdded);
		this.towerSelected = towerSelected;
		this.levelSelected = levelSelected;
//		this.effectManager = effectManager;
	}

	public Integer getTowerSelected() {
		return towerSelected;
	}

	public Integer getLevelSelected() {
		return levelSelected;
	}

//	public EffectManager getEffectManager() {
//		return effectManager;
//	}
}
