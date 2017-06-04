package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class ChangeToPrivilege extends ImmediateEffect implements Serializable {

	private static final long serialVersionUID = 8971268430299259238L;
	private Resource exchangedResource;
	private Integer councilPrivilege;
	
	public Resource getExchangedResource() {
		return exchangedResource;
	}

//	public void setExchangedResource(Resource exchangedResource) {
//		this.exchangedResource = exchangedResource;
//	}
	
	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
	
//	public void setCouncilPrivilege(Integer councilPrivilege) {
//		this.councilPrivilege = councilPrivilege;
//	}
	
	
	
}
