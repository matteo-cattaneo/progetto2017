package it.polimi.ingsw.LM22.model;

public class TerritoryCard extends DevelopmentCard {

	private final HarvestEffect permanentEffect;

	public TerritoryCard(String name, Integer period, IEffect immediateEffect,
			HarvestEffect permanentEffect) {
		super(name, period, immediateEffect);
		this.permanentEffect = permanentEffect;
	}

	public HarvestEffect getPermanentEffect() {
		return permanentEffect;
	}

}
