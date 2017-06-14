package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class NoOccupiedTowerEffect extends LeaderEffect implements Serializable{

	private static final long serialVersionUID = 7251071289750124422L;

	@Override
	public String getInfo() {
		return "If you go on an occupied tower you won't have to pay the additional cost%n";
	}

}
