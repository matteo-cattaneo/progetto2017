package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class CouncilPrivilege  implements Serializable {

	private static final long serialVersionUID = 8873036786410962291L;
	private Resource[] reward;

	public Resource getReward(Integer i) {
		return reward[i];
	}
	
	
}
