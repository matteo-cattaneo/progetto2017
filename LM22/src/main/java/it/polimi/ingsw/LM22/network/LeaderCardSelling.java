package it.polimi.ingsw.LM22.network;

import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class LeaderCardSelling extends AbstractMove {
	private LeaderCard leaderCard;

	public LeaderCardSelling(LeaderCard leaderCard) {
		this.leaderCard = leaderCard;
	}

	public LeaderCard getLeaderCard() {
		return leaderCard;
	}

}
