package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class NoFirstTurnEx extends ExEffect implements Serializable{

	private static final long serialVersionUID = -781438740930619688L;

	@Override
	public String getInfo() {
		return "You cannot do your first turn of each round, you will recover it at the end of the round";
	}

}
