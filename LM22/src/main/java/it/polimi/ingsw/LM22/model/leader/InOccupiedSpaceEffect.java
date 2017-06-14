package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class InOccupiedSpaceEffect extends LeaderEffect implements Serializable{

	private static final long serialVersionUID = 3822716778836505582L;

	@Override
	public String getInfo() {
		return "You will be able to put your family members in spaces already occupied%n";
	}

}
