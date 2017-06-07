package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class TerritoryCard extends DevelopmentCard  implements Serializable {

	private static final long serialVersionUID = 2703079994705550507L;
	private Integer requirement;
	transient private ResourcePrivilegeEffect permanentEffect;

	public ResourcePrivilegeEffect getPermanentEffect() {
		return permanentEffect;
	}

	public Integer getRequirement() {
		return requirement;
	}
}
