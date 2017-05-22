package it.polimi.ingsw.LM22.model;

public class HarvestEffect extends ResourcePrivilegeEffect {
	private Integer requirement;

	public HarvestEffect(Integer councilPrivilege, Resource resource, Integer requirement) {
		super(councilPrivilege, resource);
		this.requirement = requirement;
	}

	public Integer getRequirement() {
		return requirement;
	}
}
