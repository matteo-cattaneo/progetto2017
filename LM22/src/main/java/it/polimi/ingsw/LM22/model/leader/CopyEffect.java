package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class CopyEffect extends LeaderEffect implements Serializable{

	private static final long serialVersionUID = 5574226658714636521L;

	@Override
	public String getInfo() {
		return "You can copy the effect of a Leader Card already played by another Player of your game%n"
				+ "once you have chosen the effect is no more changeable%n";
	}

}
