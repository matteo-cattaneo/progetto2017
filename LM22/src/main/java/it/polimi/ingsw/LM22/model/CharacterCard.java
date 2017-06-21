package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CharacterCard extends DevelopmentCard implements Serializable {

	private static final long serialVersionUID = -260347990276759602L;
	private Resource coinsCost;
	private PermanentEffect permanentEffect;

	public Resource getCost() {
		return coinsCost;
	}

	public PermanentEffect getPermanentEffect() {
		return permanentEffect;
	}

	@Override
	public String getInfo() {
		String info;
		info = "Name: " + getName() + "%n";
		info = info + "Period: " + getPeriod() + "%n";
		info = info + "Card cost:%n" + getCost().getInfo();
		info = info + "Immediate effect:%n" + getImmediateEffect().getInfo();
		info = info + "Permament effect:%n" + getPermanentEffect().getInfo();
		info = info + "Card type: Character%n";
		return info;
	}
}
