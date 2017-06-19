package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class WorkMalusEx extends ExEffect implements Serializable {

	private static final long serialVersionUID = 4944033841150657858L;
	private String typeOfWork;
	private Integer valueOfMalus;

	public String getTypeOfWork() {
		return typeOfWork;
	}

	public Integer getValueOfMalus() {
		return valueOfMalus;
	}

	@Override
	public String getInfo() {
		return "You will have a malus of " + valueOfMalus + " for " + typeOfWork + " actions";
	}

}
