package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class PersonalBonusTile  implements Serializable {
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

	public ResourcePrivilegeEffect getProductionEffect() {
		return productionEffect;
	}

	public ResourcePrivilegeEffect getHarvestEffect() {
		return harvestEffect;
	}

	public Integer getRequirement() {
		return requirement;
	}

}
