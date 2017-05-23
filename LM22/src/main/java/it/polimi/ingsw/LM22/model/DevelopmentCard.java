package it.polimi.ingsw.LM22.model;

public abstract class DevelopmentCard {

	private String name;
	private Integer period;
	private IEffect immediateEffect;

	public String getName() {
		return name;
	}

	public Integer getPeriod() {
		return period;
	}

	public IEffect getImmediateEffect() {
		return immediateEffect;
	}

}
