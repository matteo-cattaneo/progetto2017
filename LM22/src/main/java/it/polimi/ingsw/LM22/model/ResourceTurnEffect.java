package it.polimi.ingsw.LM22.model;

public class ResourceTurnEffect implements TurnEffect{
	
	private final Resource reward;
	private final Integer councilPrivilege;
	
	public ResourceTurnEffect(Resource reward, Integer councilPrivilege){
		this.reward = reward;
		this.councilPrivilege = councilPrivilege;
	}

	public Resource getReward() {
		return reward;
	}

	public Integer getCouncilPrivilege() {
		return councilPrivilege;
	}
	
	
	

}
