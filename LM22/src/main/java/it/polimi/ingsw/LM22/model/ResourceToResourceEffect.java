package it.polimi.ingsw.LM22.model;

public class ResourceToResourceEffect implements IEffect{
	Resource requirement;
	Resource reward;
	Integer divider;

	public ResourceToResourceEffect(Resource requirement, Resource reward, Integer divider) {
		this.requirement = requirement;
		this.reward = reward;
		this.divider = divider;
	}

	public Resource getRequirement() {
		return requirement;
	}

	public Resource getReward() {
		return reward;
	}

	public Integer getDivider() {
		return divider;
	}
}
