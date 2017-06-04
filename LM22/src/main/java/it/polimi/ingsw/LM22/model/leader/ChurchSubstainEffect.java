package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

import it.polimi.ingsw.LM22.model.Resource;

public class ChurchSubstainEffect extends LeaderEffect implements Serializable{

	private static final long serialVersionUID = 4381584669550767439L;
	private Resource reward;

	public Resource getReward() {
		return reward;
	}
	
	
}
