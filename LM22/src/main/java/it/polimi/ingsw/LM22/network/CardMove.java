package it.polimi.ingsw.LM22.network;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Resource;

public class CardMove extends MemberMove {
	private String towerSelected;
	private Integer levelSelected;
//	private EffectManager effectManager;

	public CardMove(FamilyMember memberUsed, Resource servantsAdded, String towerSelected, Integer levelSelected/*,
			EffectManager effectManager*/) {
		super(memberUsed, servantsAdded);
		this.towerSelected = towerSelected;
		this.levelSelected = levelSelected;
//		this.effectManager = effectManager;
	}

	public String getTowerSelected() {
		return towerSelected;
	}

	public Integer getLevelSelected() {
		return levelSelected;
	}

//	public EffectManager getEffectManager() {
//		return effectManager;
//	}
}
