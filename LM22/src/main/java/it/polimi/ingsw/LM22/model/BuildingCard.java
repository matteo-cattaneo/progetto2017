package it.polimi.ingsw.LM22.model;

public class BuildingCard extends DevelopmentCard {

	private Resource cardCost;
	private ProductionEffect permanentEffect;

	public Resource getCost() {
		return cardCost;
	}

	public ProductionEffect getPermanentEffect() {
		return permanentEffect;
	}
}
