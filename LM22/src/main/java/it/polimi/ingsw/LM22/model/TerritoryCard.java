package it.polimi.ingsw.LM22.model;

public class TerritoryCard extends DevelopmentCard{
	
	private final HarvestEffect permanentEffect;
	
	public TerritoryCard(String name, Integer period, String immediateType/*, Ieffect*/, HarvestEffect permanentEffect){
		super(name, period, immediateType);
		this.permanentEffect = permanentEffect;
	}
	
	
	public HarvestEffect getPermanentEffect(){
		return permanentEffect;
	}

}
