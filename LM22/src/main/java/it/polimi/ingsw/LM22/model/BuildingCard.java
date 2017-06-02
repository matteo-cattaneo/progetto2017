package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class BuildingCard extends DevelopmentCard  implements Serializable {

	private Resource cardCost;
	private Integer requirement;
	private ImmediateEffect permanentEffect;

	public Resource getCost() {
		return cardCost;
	}
	
	public Integer getRequirement(){
		return requirement;
	}

	public ImmediateEffect getPermanentEffect() {
		return permanentEffect;
	}
}
