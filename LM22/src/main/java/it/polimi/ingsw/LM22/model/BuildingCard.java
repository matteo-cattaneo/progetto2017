package it.polimi.ingsw.LM22.model;

public class BuildingCard extends DevelopmentCard {

	private final Resource cardCost;
	private final ProductionEffect permanentEffect;

	public BuildingCard(String name, Integer period, IEffect immediateEffect, Resource cardCost, ProductionEffect permanentEffect) {
		super(name, period, immediateEffect);
		this.cardCost = cardCost;
		this.permanentEffect = permanentEffect;
	}

	public Resource getCost() {
		return cardCost;
	}

	public ProductionEffect getPermanentEffect() {
		return permanentEffect;
	}
}
