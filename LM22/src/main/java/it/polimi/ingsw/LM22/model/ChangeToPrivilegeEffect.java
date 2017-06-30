package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class ChangeToPrivilegeEffect extends ImmediateEffect implements Serializable  {

	private static final long serialVersionUID = 2164988098962245810L;
	private Resource exchangedResource;
	private Integer councilPrivilege;

	public Resource getExchangedResource() {
		return exchangedResource;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
	
	@Override
	public String getInfo() {
		String info = "";
		info = info + "You can change " + exchangedResource.getInfo() + " into " + councilPrivilege
				+ " councilPrivilege(s)%n";
		return info;
	}
}
