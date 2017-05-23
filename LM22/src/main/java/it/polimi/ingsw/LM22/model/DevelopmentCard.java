package it.polimi.ingsw.LM22.model;

public abstract class DevelopmentCard {

	private final String name;
	private final Integer period;
	private final IEffect immediateEffect;
	

	public DevelopmentCard(String name, Integer period, IEffect immediateEffect) {
		this.name = name;
		this.period = period;
		/* qui probabilmente ci va il costruttore del tipo */
		this.immediateEffect = immediateEffect; 
	}

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
