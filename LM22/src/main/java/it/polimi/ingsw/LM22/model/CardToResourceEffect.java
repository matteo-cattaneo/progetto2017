package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CardToResourceEffect extends ImmediateEffect implements Serializable {
	private String cardRequired;
	private Resource reward;

	public String getCardRequired() {
		return cardRequired;
	}

	public Resource getReward() {
		return reward;
	}

}
