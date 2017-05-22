package it.polimi.ingsw.LM22.model;

public class ResourcePrivilegeEffect implements IEffect {
	private Integer councilPrivilege;
	private Resource resource;

	public ResourcePrivilegeEffect(Integer councilPrivilege, Resource resource) {
		this.councilPrivilege = councilPrivilege;
		this.resource = resource;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}

	public Resource getResource() {
		return resource;
	}

}
