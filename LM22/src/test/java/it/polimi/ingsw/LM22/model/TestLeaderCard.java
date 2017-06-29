package it.polimi.ingsw.LM22.model;

import java.io.IOException;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.leader.CardRequest;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import it.polimi.ingsw.LM22.model.leader.LeaderResourceEffect;
import it.polimi.ingsw.LM22.model.leader.ResourceRequest;
import it.polimi.ingsw.LM22.model.leader.WorkAction;
import junit.framework.TestCase;

public class TestLeaderCard extends TestCase {
	FileParser fp;
	Game game;

	public void setUp() {
		fp = new FileParser();
		game = new Game();
	}

	@Test
	public void testFirstLeaderCard() throws IOException {
		fp.getLeaderCards(game);
		assertEquals(20, game.getLeaderCards().size());

		LeaderCard leader = game.getLeaderCards().get(0);
		assertEquals("Francesco Sforza", leader.getName());
		assertNotNull(leader.getRequest());
		assertEquals(0, ((CardRequest) leader.getRequest()).getBuildingCards().intValue());
		assertEquals(0, ((CardRequest) leader.getRequest()).getCharacterCards().intValue());
		assertEquals(0, ((CardRequest) leader.getRequest()).getTerritoryCards().intValue());
		assertEquals(5, ((CardRequest) leader.getRequest()).getVentureCards().intValue());
		assertNotNull(leader.getEffect());
		assertEquals(1, ((WorkAction) leader.getEffect()).getValueOfWork().intValue());
		assertEquals("HARVEST", ((WorkAction) leader.getEffect()).getTypeOfWork());
		assertEquals(
				"Name: Francesco Sforza%nRequest:%nYou must have%nEffect:%n"
						+ "You are able to do a HARVEST action with a value of 1%nCard type: Leader%n",
				leader.getInfo());
	}

	@Test
	public void testSecondLeaderCard() throws IOException {
		fp.getLeaderCards(game);
		assertEquals(20, game.getLeaderCards().size());
		LeaderCard leader = game.getLeaderCards().get(4);
		assertEquals("Girolamo Savonarola", leader.getName());
		assertNotNull(leader.getRequest());
		assertNotNull(((ResourceRequest) leader.getRequest()).getResource());
		assertNotNull(leader.getEffect());
		assertNotNull(((LeaderResourceEffect) leader.getEffect()).getResource());
		assertEquals(0, ((LeaderResourceEffect) leader.getEffect()).getCouncilPrivilege().intValue());
		assertEquals("Name: Girolamo Savonarola%nRequest:%nYou must have%ncoins: 18%n"
				+ "Effect:%nYou will get faith: 1%nCard type: Leader%n", leader.getInfo());
	}
}
