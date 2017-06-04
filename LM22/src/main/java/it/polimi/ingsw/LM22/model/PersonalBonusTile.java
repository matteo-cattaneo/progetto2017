package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class PersonalBonusTile implements Serializable {
	
	private static final long serialVersionUID = 2823653239917665044L;
	private Integer requirement;
	// costanti da inizializzare con il costruttore
	private ResourcePrivilegeEffect productionEffect;
	private ResourcePrivilegeEffect harvestEffect;

	/*
	 * i bonus anche BONUSTILE devono essere caricati di file
	 */

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
