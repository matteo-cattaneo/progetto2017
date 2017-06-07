package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class LeaderCard implements Serializable {

	private static final long serialVersionUID = -3480796713023783579L;
	private String name;
	transient private LeaderCardRequest request;
	transient private LeaderEffect effect;

	public String getName() {
		return name;
	}

	public LeaderCardRequest getRequest() {
		return request;
	}

	public LeaderEffect getEffect() {
		return effect;
	}

}
