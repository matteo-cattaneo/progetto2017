package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class LeaderCardActivation extends AbstractMove {
	private LeaderCard leaderCard;
	private Resource servants;
	private String color;

	public LeaderCardActivation(Player p, LeaderCard leaderCard, Resource servants, String color) {
		super(p);
		this.leaderCard = leaderCard;
		this.servants = servants;
		this.color = color;
	}

	public Resource getServants() {
		return servants;
	}

	public String getColor() {
		return color;
	}

	public LeaderCard getLeaderCard() {
		return leaderCard;
	}

}
