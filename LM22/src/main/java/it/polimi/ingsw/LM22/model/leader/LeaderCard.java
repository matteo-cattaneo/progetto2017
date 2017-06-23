package it.polimi.ingsw.LM22.model.leader;

import java.io.Serializable;

public class LeaderCard implements Serializable {

	private static final long serialVersionUID = -3480796713023783579L;
	private String name;
	private LeaderCardRequest request;
	private LeaderEffect effect;

	public String getName() {
		return name;
	}

	public LeaderCardRequest getRequest() {
		return request;
	}

	public void setEffect(LeaderEffect effect) {
		this.effect = effect;
	}

	public LeaderEffect getEffect() {
		return effect;
	}

	public String getInfo() {
		String info;
		info = "Name: " + getName() + "%n";
		info = info + "Request:%n" + getRequest().getInfo();
		info = info + "Effect:%n" + getEffect().getInfo();
		info = info + "Card type: Leader%n";
		return info;
	}
}
