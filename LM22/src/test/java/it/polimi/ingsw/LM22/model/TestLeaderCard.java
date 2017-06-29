package it.polimi.ingsw.LM22.model;

import java.io.IOException;

import org.junit.Test;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import junit.framework.TestCase;

public class TestLeaderCard extends TestCase {
	FileParser fp;
	Game game;

	public void setUp() {
		fp = new FileParser();
		game = new Game();
	}

	@Test
	public void testLeaderCards() throws IOException {
		fp.getLeaderCards(game);
		assertEquals(20, game.getLeaderCards().size());

		LeaderCard leader = game.getLeaderCards().get(0);
		assertEquals("Francesco Sforza", leader.getName());
		assertNotNull(leader.getRequest());
		assertNotNull(leader.getEffect());
		assertEquals(
				"Name: Francesco Sforza%nRequest:%nYou must have%nEffect:%n"
						+ "You are able to do a HARVEST action with a value of 1%nCard type: Leader%n",
				leader.getInfo());
	}
}
