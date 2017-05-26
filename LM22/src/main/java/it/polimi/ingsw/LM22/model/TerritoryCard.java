package it.polimi.ingsw.LM22.model;

public class TerritoryCard extends DevelopmentCard {

	private Integer requirement;
	private ResourcePrivilegeEffect permanentEffect;

	public ResourcePrivilegeEffect getPermanentEffect() {
		return permanentEffect;
	}

	public Integer getRequirement() {
		return requirement;
	}
}
