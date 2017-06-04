package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class ResourcePrivilegeEffect extends ImmediateEffect  implements Serializable {

	private static final long serialVersionUID = 6513656682052729182L;
	private Integer councilPrivilege;
	private Resource resource;

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}

	public Resource getResource() {
		return resource;
	}

}
