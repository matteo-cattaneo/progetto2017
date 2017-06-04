package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class PersonalBonusTile implements Serializable {
	private Integer requirement;
	// costanti da inizializzare con il costruttore
	private Resource productionEffect;
	private Resource harvestEffect;

	/*
	 * i bonus anche BONUSTILE devono essere caricati di file
	 */

	public Resource getProductionEffect() {
		return productionEffect;
	}

	public Resource getHarvestEffect() {
		return harvestEffect;
	}

	public Integer getRequirement() {
		return requirement;
	}

}
