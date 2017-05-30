package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class LeaderCardActivation extends AbstractMove {
	private LeaderCard leaderCard;

	public LeaderCardActivation(Player p, LeaderCard leaderCard) {
		super(p);
		this.leaderCard = leaderCard;
	}
	
	public LeaderCard getLeaderCard() {
		return leaderCard;
	}

}
