package it.polimi.ingsw.LM22.model;

public class ChangeEffect {
	Resource exchangeEffect1[];
	Resource exchangeEffect2[];
	Integer requirement;

	public ChangeEffect(Resource exchangeEffect1[], Resource exchangeEffect2[], Integer requirement) {
		this.exchangeEffect1 = exchangeEffect1;
		this.exchangeEffect2 = exchangeEffect2;
		this.requirement = requirement;
	}

	public Resource[] getExchangeEffect1() {
		return exchangeEffect1;
	}

	public Resource[] getExchangeEffect2() {
		return exchangeEffect2;
	}

	public Integer getRequirement() {
		return requirement;
	}

}
