package it.polimi.ingsw.LM22.model;

public class ResourceToResourceEffect implements IEffect{
	private Resource requirement;
	private Resource reward;
	private Integer divider;

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
