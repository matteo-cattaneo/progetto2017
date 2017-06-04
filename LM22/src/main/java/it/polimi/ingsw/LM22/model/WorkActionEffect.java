package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class WorkActionEffect extends ImmediateEffect  implements Serializable {

	private static final long serialVersionUID = 8990685592002534616L;
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
