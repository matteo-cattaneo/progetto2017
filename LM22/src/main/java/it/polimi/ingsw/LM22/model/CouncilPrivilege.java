package it.polimi.ingsw.LM22.model;

public class CouncilPrivilege {
	private final Resource[] reward;
	
	public CouncilPrivilege(Resource[] reward){
		this.reward = reward;
	}

	public Resource getReward(Integer i) {
		return reward[i];
	}
	
	
}
