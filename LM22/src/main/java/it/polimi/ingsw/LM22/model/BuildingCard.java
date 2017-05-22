package it.polimi.ingsw.LM22.model;

public class BuildingCard extends DevelopmentCard{
	
	public final Resource cardCost;
	public final ProductionEffect permanentEffect;
	
	public BuildingCard(String name, Integer period, String immediateType/*, IEffect*/, Resource cardCost, ProductionEffect permanentEffect){
		super(name, period, immediateType);
		this.cardCost = cardCost;
		this.permanentEffect = permanentEffect;
	}
	
	public Resource getCost(){
		return cardCost;
	}
	
	public ProductionEffect getPermanentEffect(){
		return permanentEffect;
	}
}
