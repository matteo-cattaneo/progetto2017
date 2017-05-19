package it.polimi.ingsw.LM22.model;

public class PersonalBonusTile {
	private final Integer requirement = 1;
	//costanti da inizializzare con il costruttore
	private final ProductionEffect productionEffect;
	private final HarvestEffect harvestEffect;
	
	
	public ProductionEffect getProductionEffect() {
		return productionEffect;
	}
	public HarvestEffect getHarvestEffect() {
		return harvestEffect;
	}
	
	public Integer getRequirement(){
		return requirement;
	}
	
}
