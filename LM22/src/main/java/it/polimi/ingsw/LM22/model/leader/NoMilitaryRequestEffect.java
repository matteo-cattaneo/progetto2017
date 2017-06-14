package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class NoMilitaryRequestEffect extends LeaderEffect implements Serializable{

	private static final long serialVersionUID = -2831349666570071852L;

	@Override
	public String getInfo() {
		return "You won't have no more Military points requests to get Territory cards%n";
	}

}
