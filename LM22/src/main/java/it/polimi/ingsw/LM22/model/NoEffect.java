package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class NoEffect extends ImmediateEffect implements Serializable {

	private static final long serialVersionUID = 6423189074376174807L;

	@Override
	public String getInfo() {
		return "No Effect%n";
	}

}
