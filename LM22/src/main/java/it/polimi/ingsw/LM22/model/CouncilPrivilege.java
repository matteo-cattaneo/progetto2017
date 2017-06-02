package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CouncilPrivilege  implements Serializable {
	private Resource[] reward;

	public Resource getReward(Integer i) {
		return reward[i];
	}
	
	
}
