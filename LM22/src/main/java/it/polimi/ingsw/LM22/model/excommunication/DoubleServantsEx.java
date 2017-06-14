package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class DoubleServantsEx extends ExEffect implements Serializable{

	private static final long serialVersionUID = -7023234922871455453L;

	@Override
	public String getInfo() {
		return "You have to give 2 servants to have a bonus of 1";
	}

}
