package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class LeaderResourceEffect extends LeaderEffect implements Serializable{

	private Resource resource;
	private Integer councilPrivilege;
	public Resource getResource() {
		return resource;
	}
	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
	
	
}
