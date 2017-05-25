package it.polimi.ingsw.LM22.model;

public class CharacterCard extends DevelopmentCard {
	private Resource coinsCost;
	private PermanentEffect permanentEffect;

	public Resource getCost() {
		return coinsCost;
	}

	public PermanentEffect getPermanentEffect() {
		return permanentEffect;
	}
}
