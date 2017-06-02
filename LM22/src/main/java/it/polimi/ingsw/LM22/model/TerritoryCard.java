package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class TerritoryCard extends DevelopmentCard  implements Serializable {

	private Integer requirement;
	private ResourcePrivilegeEffect permanentEffect;

	public ResourcePrivilegeEffect getPermanentEffect() {
		return permanentEffect;
	}

	public Integer getRequirement() {
		return requirement;
	}
}
