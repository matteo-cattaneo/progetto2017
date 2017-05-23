package it.polimi.ingsw.LM22.model;

public class CharacterCard extends DevelopmentCard {
	private Resource coinsCost;
	private CharacterEffect permanentEffect;

	public Resource getCost() {
		return coinsCost;
	}

	public CharacterEffect getPermanentEffect() {
		return permanentEffect;
	}
}
