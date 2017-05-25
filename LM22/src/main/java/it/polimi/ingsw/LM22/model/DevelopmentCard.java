package it.polimi.ingsw.LM22.model;

public abstract class DevelopmentCard {

	private String name;
	private Integer period;
	private ImmediateEffect immediateEffect;

	public String getName() {
		return name;
	}

	public Integer getPeriod() {
		return period;
	}

	public ImmediateEffect getImmediateEffect() {
		return immediateEffect;
	}

}
