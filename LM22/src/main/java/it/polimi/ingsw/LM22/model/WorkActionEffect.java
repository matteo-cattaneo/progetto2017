package it.polimi.ingsw.LM22.model;

public class WorkActionEffect implements IEffect {
	private Integer workActionValue;
	private String typeOfWork;
	private Resource resource;
	private Integer councilPrivilege;

	public Integer getWorkActionValue() {
		return workActionValue;
	}

	public String getTypeOfWork() {
		return typeOfWork;
	}

	public Resource getResource() {
		return resource;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
}
