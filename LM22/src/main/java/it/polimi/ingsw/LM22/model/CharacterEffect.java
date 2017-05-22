package it.polimi.ingsw.LM22.model;

public class CharacterEffect {
	private final boolean noCardSpaceBonusEffect;
	private final ColorCardBonusEffect colorCardBonusEffect;
	private final WorkBonusEffect workBonusEffect;

	public CharacterEffect(boolean noCardSpaceBonusEffect, ColorCardBonusEffect colorCardBonusEffect,
			WorkBonusEffect workBonusEffect) {
		this.noCardSpaceBonusEffect = noCardSpaceBonusEffect;
		this.colorCardBonusEffect = colorCardBonusEffect;
		this.workBonusEffect = workBonusEffect;
	}

	public boolean isNoCardSpaceBonusEffect() {
		return noCardSpaceBonusEffect;
	}

	public ColorCardBonusEffect getColorCardBonusEffect() {
		return colorCardBonusEffect;
	}

	public WorkBonusEffect getWorkBonusEffect() {
		return workBonusEffect;
	}
	

}
