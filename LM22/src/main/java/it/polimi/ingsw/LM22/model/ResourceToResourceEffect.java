package it.polimi.ingsw.LM22.model;

import java.io.Serializable;

public class ResourceToResourceEffect extends ImmediateEffect implements Serializable {
	
	private static final long serialVersionUID = 7050936495635283733L;
	private Resource requirement;
	private Resource reward;

	public Resource getRequirement() {
		return requirement;
	}

	public Resource getReward() {
		return reward;
	}

	@Override
	public String getInfo() {
		String info ="";
		info = info + "for every " + requirement.getInfo() + "you have, you earn " + reward.getInfo();
		return info;
	}

}
