package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class DoubleResourceEffect extends LeaderEffect implements Serializable {

	private static final long serialVersionUID = -1571797970887779059L;
	private Resource resourceMoltiplicator;

	public Resource getResourceMoltiplicator() {
		return resourceMoltiplicator;
	}

	@Override
	public String getInfo() {
		return "Everytime you earn resources due to an immediate effect of a "
				+ "development card you earn the reward moltiplicated by " + resourceMoltiplicator.getInfo();
	}

}
