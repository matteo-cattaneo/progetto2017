package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class BuildingCard extends DevelopmentCard  implements Serializable {


	private static final long serialVersionUID = 1129061721831386161L;
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
