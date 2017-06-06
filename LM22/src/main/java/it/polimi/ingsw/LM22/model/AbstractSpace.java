package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public abstract class AbstractSpace implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Integer spaceRequirement;

	public Integer getSpaceRequirement() {
		return spaceRequirement;
	}

	public void setSpaceRequirement(Integer spaceRequirement) {
		this.spaceRequirement = spaceRequirement;
	}

}
