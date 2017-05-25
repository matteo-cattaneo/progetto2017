package it.polimi.ingsw.LM22.model;

public class PersonalBonusTile {
	private final Integer requirement = 1;
	// costanti da inizializzare con il costruttore
	private final ResourcePrivilegeEffect productionEffect;
	private final ResourcePrivilegeEffect harvestEffect;

	
	/*
	 * i bonus anche BONUSTILE devono essere caricati di file
	 * --> ATTENZIONE al costruttore
	 * */
	public PersonalBonusTile(ResourcePrivilegeEffect productionEffect, ResourcePrivilegeEffect harvestEffect) {
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
