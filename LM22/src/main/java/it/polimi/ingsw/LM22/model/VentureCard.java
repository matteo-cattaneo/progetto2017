package it.polimi.ingsw.LM22.model;

public class VentureCard extends DevelopmentCard {

	private Resource cardCost1;
	private Resource[] cardCost2;
	private Resource permanentEffect;

	public Resource getCardCost1() {
		return cardCost1;
	}

	public Resource[] getCardCost2() {
		return cardCost2;
	}

	public Resource getPermanentEffect() {
		return permanentEffect;
	}

	public void selectCardCost() {
		//TO-DO
	}

}
