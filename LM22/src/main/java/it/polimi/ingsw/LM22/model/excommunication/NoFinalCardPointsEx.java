package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class NoFinalCardPointsEx extends ExEffect implements Serializable {

	private static final long serialVersionUID = 4795244427015942634L;
	private Integer cardType;

	public Integer getCardType() {
		return cardType;
	}

	@Override
	public String getInfo() {
		String info = "";
		info = info + "You don't get your final Points for the cards you have of the " + (cardType+1) + "  tower";
		return info;
	}

}
