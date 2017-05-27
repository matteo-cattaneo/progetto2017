package it.polimi.ingsw.LM22.network;

import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class LeaderCardActivation extends AbstractMove {
	private LeaderCard leaderCard;

	public LeaderCardActivation(LeaderCard leaderCard) {
		this.leaderCard = leaderCard;
	}
	
	public LeaderCard getLeaderCard() {
		return leaderCard;
	}

}
