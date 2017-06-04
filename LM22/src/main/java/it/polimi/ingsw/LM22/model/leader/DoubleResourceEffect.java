package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class DoubleResourceEffect extends LeaderEffect implements Serializable{

	private static final long serialVersionUID = -1571797970887779059L;
	private Resource resourceMoltiplicator;

	public Resource getResourceMoltiplicator() {
		return resourceMoltiplicator;
	}
	
	
}
