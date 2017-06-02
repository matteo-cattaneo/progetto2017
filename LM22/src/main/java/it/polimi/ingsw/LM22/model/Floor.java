package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class Floor  implements Serializable {
	//inizializzare (valutare il caricamento da file)
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
