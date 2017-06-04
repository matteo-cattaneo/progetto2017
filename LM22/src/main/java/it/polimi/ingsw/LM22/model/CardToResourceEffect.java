package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CardToResourceEffect extends ImmediateEffect implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2651506056797918980L;
	private String cardRequired;
	private Resource reward;

	public String getCardRequired() {
		return cardRequired;
	}

	public Resource getReward() {
		return reward;
	}

}
