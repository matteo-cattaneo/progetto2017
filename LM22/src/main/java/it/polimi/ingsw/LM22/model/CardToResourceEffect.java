package it.polimi.ingsw.LM22.model;

public class CardToResourceEffect {
	String cardRequired;
	Resource reward;

	public CardToResourceEffect(String cardRequired, Resource reward) {
		this.cardRequired = cardRequired;
		this.reward = reward;
	}

	public String getCardRequired() {
		return cardRequired;
	}

	public Resource getReward() {
		return reward;
	}

}
