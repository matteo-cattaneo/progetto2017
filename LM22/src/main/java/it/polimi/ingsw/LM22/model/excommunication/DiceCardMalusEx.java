package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class DiceCardMalusEx extends ExEffect implements Serializable {

	private static final long serialVersionUID = -7803859850348672641L;
	private Integer cardType;
	private Integer malus;

	public Integer getMalus() {
		return malus;
	}

	public Integer getCardType() {
		return cardType;
	}

	@Override
	public String getInfo() {
		String info = "";
		info = info + "You get a malus of " + malus + " for the " + (cardType + 1) + " tower";
		return info;
	}
}
