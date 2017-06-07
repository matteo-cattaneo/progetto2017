package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public abstract class DevelopmentCard  implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private Integer period;
	transient private ImmediateEffect immediateEffect;

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
