package it.polimi.ingsw.LM22.model;

public class WorkActionEffect implements IEffect {
	Integer workActionValue;
	String typeOfWork;
	Resource resource;
	Integer councilPrivilege;

	public WorkActionEffect(Integer workActionValue, String typeOfWork, Resource resource, Integer councilPrivilege) {
		this.workActionValue = workActionValue;
		this.typeOfWork = typeOfWork;
		this.resource = resource;
		this.councilPrivilege = councilPrivilege;
	}

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
