package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class ChangeToPrivilegeEffect extends ImmediateEffect implements Serializable  {
	private Resource exchangedResource;
	private Integer councilPrivilege;

	public Resource getExchangedResource() {
		return exchangedResource;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
}
