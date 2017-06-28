package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class Floor  implements Serializable {

	private static final long serialVersionUID = -9002150340093349610L;
	private CardSpace space; 
	private DevelopmentCard card;

	public CardSpace getSpace() {
		return space;
	}

	public DevelopmentCard getCard() {
		return card;
	}

	public void setSpace(CardSpace space) {
		this.space = space;
	}

	public void setCard(DevelopmentCard card) {
		this.card = card;
	}
}
