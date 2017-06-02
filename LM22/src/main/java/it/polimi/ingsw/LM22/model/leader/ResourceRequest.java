package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class ResourceRequest extends LeaderCardRequest implements Serializable{

	private Resource resource;

	public Resource getResource() {
		return resource;
	}
	
	
	
}
