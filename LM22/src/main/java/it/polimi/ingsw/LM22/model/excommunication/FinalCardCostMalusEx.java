package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class FinalCardCostMalusEx extends FinalResourceMalusEx implements Serializable{

	private static final long serialVersionUID = -850332193094560511L;
	private Integer cardType;

	public Integer getCardType() {
		return cardType;
	}
	
	@Override
	public String getInfo() {
		return "You get a malus of " + resource.getInfo() + "due to your" + (cardType+1) + "cost";
	}
	
}
