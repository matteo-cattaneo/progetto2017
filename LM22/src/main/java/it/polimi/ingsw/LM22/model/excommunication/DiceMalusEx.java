package it.polimi.ingsw.LM22.model.excommunication;

import java.io.Serializable;

public class DiceMalusEx extends ExEffect implements Serializable{

	private static final long serialVersionUID = 5112483506098349041L;
	private Integer malus;

	public Integer getMalus() {
		return malus;
	}
	
	public String getInfo() {
		String info = "";
		info = info + "You get a malus of " + malus + " for all your colored Members";
		return info;
	}
	
}
