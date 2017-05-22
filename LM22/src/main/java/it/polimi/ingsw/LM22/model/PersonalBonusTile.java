package it.polimi.ingsw.LM22.model;

public class PersonalBonusTile {
	private final Integer requirement;
	// costanti da inizializzare con il costruttore
	private final ProductionEffect productionEffect;
	private final HarvestEffect harvestEffect;

	public PersonalBonusTile(Integer requirement, ProductionEffect productionEffect, HarvestEffect harvestEffect) {
		this.requirement = requirement;
		this.productionEffect = productionEffect;
		this.harvestEffect = harvestEffect;
	}

	public ProductionEffect getProductionEffect() {
		return productionEffect;
	}

	public HarvestEffect getHarvestEffect() {
		return harvestEffect;
	}

	public Integer getRequirement() {
		return requirement;
	}

}
