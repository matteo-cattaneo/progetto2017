package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class VentureCard extends DevelopmentCard  implements Serializable {

	private static final long serialVersionUID = 5037136863567493067L;
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
