package it.polimi.ingsw.LM22.network;

import it.polimi.ingsw.LM22.controller.AbstractMove;
import it.polimi.ingsw.LM22.controller.CardMove;
import it.polimi.ingsw.LM22.controller.CouncilMove;
import it.polimi.ingsw.LM22.controller.EndMove;
import it.polimi.ingsw.LM22.controller.LeaderCardActivation;
import it.polimi.ingsw.LM22.controller.LeaderCardSelling;
import it.polimi.ingsw.LM22.controller.MarketMove;
import it.polimi.ingsw.LM22.controller.WorkMove;
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
		String[] param = sMove.split("@");
		// select the right move type and initialize with the right dynamic type
		switch (param[0]) {
		case "LeaderAct":
			objMove = new LeaderCardActivation(p, getLeaderCard(param[1]), getServantsAdded(param[2]), param[3]);
			break;
		case "LeaderSell":
			objMove = new LeaderCardSelling(p, getLeaderCard(param[1]));
			break;
		case "Market":
			objMove = new MarketMove(p, getFamilyMember(param[1]), getServantsAdded(param[2]),
					Integer.parseInt(param[3]));
			break;
		case "Work":
			objMove = new WorkMove(p, getFamilyMember(param[1]), getServantsAdded(param[2]), param[3]);
			break;
		case "Card":
			objMove = new CardMove(p, getFamilyMember(param[1]), getServantsAdded(param[2]), Integer.parseInt(param[3]),
					Integer.parseInt(param[4]));
			break;
		case "Council":
			objMove = new CouncilMove(p, getFamilyMember(param[1]), getServantsAdded(param[2]));
			break;
		case "End":
			objMove = new EndMove(p);
		}
		return objMove;
	}

	private LeaderCard getLeaderCard(String param) {
		// get from the player list the proper LeaderCard
		for (LeaderCard ld : player.getActivatedLeaderCards())
			if (ld.getName().equals(param))
				return ld;
		for (LeaderCard ld : player.getLeaderCards())
			if (ld.getName().equals(param))
				return ld;
		return null;
	}

	private FamilyMember getFamilyMember(String param) {
		for (FamilyMember fm : player.getMembers()) {
			if (fm.getColor().equals(param)) {
				return fm;
			}
		}
		return null;
	}

	private Resource getServantsAdded(String param) {
		Integer serv = Integer.parseInt(param);
		// create a new Resource with the specified servants quantity
		Resource resource = new Resource(0, 0, serv, 0, 0, 0, 0);
		return resource;
	}
}
