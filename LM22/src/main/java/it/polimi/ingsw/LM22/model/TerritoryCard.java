package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class TerritoryCard extends DevelopmentCard implements Serializable {

	private static final long serialVersionUID = 2703079994705550507L;
	private Integer requirement;
	private ResourcePrivilegeEffect permanentEffect;

	public ResourcePrivilegeEffect getPermanentEffect() {
		return permanentEffect;
	}

	public Integer getRequirement() {
		return requirement;
	}

	@Override
	public String getInfo() {
		String info;
		info = "Name: " + getName() + "%n";
		info = info + "Period: " + getPeriod() + "%n";
		info = info + "Immediate effect:%n" + getImmediateEffect().getInfo();
		info = info + "Permament effect:%n" + "Requirement: " + getRequirement() + "%n"
				+ getPermanentEffect().getInfo();
		info = info + "Card type: Territory%n";
		return info;
	}
}
