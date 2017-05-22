package it.polimi.ingsw.LM22.model;

public class TerritoryCard extends DevelopmentCard {

	private final HarvestEffect permanentEffect;

	public TerritoryCard(String name, Integer period, String immediateType, IEffect immediateEffect,
			HarvestEffect permanentEffect) {
		super(name, period, immediateType, immediateEffect);
		this.permanentEffect = permanentEffect;
	}

	public HarvestEffect getPermanentEffect() {
		return permanentEffect;
	}

}
