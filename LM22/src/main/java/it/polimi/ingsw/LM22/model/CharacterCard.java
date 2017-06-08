package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CharacterCard extends DevelopmentCard  implements Serializable {

	private static final long serialVersionUID = -260347990276759602L;
	private Resource coinsCost;
	private PermanentEffect permanentEffect;

	public Resource getCost() {
		return coinsCost;
	}

	public PermanentEffect getPermanentEffect() {
		return permanentEffect;
	}
}
