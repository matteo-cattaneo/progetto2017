package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class ResourcePrivilegeEffect extends ImmediateEffect  implements Serializable {
	private Integer councilPrivilege;
	private Resource resource;

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}

	public Resource getResource() {
		return resource;
	}

}
