package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class WorkMalusEx extends ExEffect implements Serializable{

	private String typeOfWork;
	private Integer valueOfMalus;
	
	public String getTypeOfWork() {
		return typeOfWork;
	}
	public Integer getValueOfMalus() {
		return valueOfMalus;
	}
	
	
}
