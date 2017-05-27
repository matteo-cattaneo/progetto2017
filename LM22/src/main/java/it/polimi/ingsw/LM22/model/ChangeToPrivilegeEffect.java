package it.polimi.ingsw.LM22.model;

public class ChangeToPrivilegeEffect extends ImmediateEffect {
	private Resource exchangedResource;
	private Integer councilPrivilege;

	public Resource getExchangedResource() {
		return exchangedResource;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
}
