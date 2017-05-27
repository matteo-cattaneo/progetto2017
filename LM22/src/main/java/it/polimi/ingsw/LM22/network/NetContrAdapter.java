package it.polimi.ingsw.LM22.network;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;

public class NetContrAdapter {
	Player player;

	public AbstractMove moveParser(Player p, String sMove) {
		AbstractMove objMove = null;
		player = p;
		// divide sMove string into multiple parameters
		String[] param = sMove.split("$");
		// select the right move type and initialize with the right dynamic type
		switch (param[0]) {
		case "LeaderAct":
			objMove = new LeaderCardActivation(getLeaderCard(param[1]));
			break;
		case "LeaderSell":
			objMove = new LeaderCardSelling(getLeaderCard(param[1]));
			break;
		case "Market":
			objMove = new MarketMove(getFamilyMember(param[1]), getServantsAdded(param[2]), Integer.parseInt(param[3]));
			break;
		case "Work":
			objMove = new WorkMove(getFamilyMember(param[1]), getServantsAdded(param[2]), param[3]);
			break;
		case "Card":
			objMove = new CardMove(getFamilyMember(param[1]), getServantsAdded(param[2]), param[3],
					Integer.parseInt(param[4]));
			break;
		}
		return objMove;
	}

	private LeaderCard getLeaderCard(String param) {
		// cast to integer value
		Integer cardN = Integer.parseInt(param);
		// get from the player list the proper LeaderCard
		LeaderCard ld = player.getLeaderCards().get(cardN);
		return ld;
	}

	private FamilyMember getFamilyMember(String param) {
		Integer famN = Integer.parseInt(param);
		FamilyMember fm = player.getMembers().get(famN);
		return fm;
	}

	private Resource getServantsAdded(String param) {
		Integer serv = Integer.parseInt(param);
		// create a new Resource with the specified servants quantity
		Resource resource = new Resource(0, 0, serv, 0, 0, 0, 0);
		return resource;
	}
}
