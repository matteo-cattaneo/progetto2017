package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class BuildingCard extends DevelopmentCard implements Serializable {

	private static final long serialVersionUID = 1129061721831386161L;
	private Resource cardCost;
	private Integer requirement;
	private ImmediateEffect permanentEffect;

	public Resource getCost() {
		return cardCost;
	}

	public Integer getRequirement() {
		return requirement;
	}

	public ImmediateEffect getPermanentEffect() {
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
		info = info + "Card type: Building%n";
		return info;
	}
}
