package it.polimi.ingsw.LM22.model.leader;

import java.io.IOException;

import org.junit.Test;

import it.polimi.ingsw.LM22.controller.FileParser;
import it.polimi.ingsw.LM22.model.Game;

import junit.framework.TestCase;

public class TestLeaderRequests extends TestCase {
	FileParser fp;
	Game game;

	public void setUp() throws IOException {
		fp = new FileParser();
		game = new Game();
		fp.getLeaderCards(game);
	}

	@Test
	public void testCardRequest() {
		CardRequest request = (CardRequest) game.getLeaderCards().get(14).getRequest();
		assertEquals(2, request.getBuildingCards().intValue());
		assertEquals(2, request.getCharacterCards().intValue());
		assertEquals(2, request.getTerritoryCards().intValue());
		assertEquals(2, request.getVentureCards().intValue());
		assertEquals(
				"You must have%n- 2 Territory cards%n- 2 Character cards%n- 2 Buildings cards%n- 2 Venture cards%n",
				request.getInfo());
	}

	@Test
	public void testResourceRequest() {
		ResourceRequest request = (ResourceRequest) game.getLeaderCards().get(4).getRequest();
		assertNotNull(request.getResource());
		assertEquals("You must have%ncoins: 18%n", request.getInfo());
	}

	@Test
	public void testResourceCardRequest() {
		ResourceCardRequest request = (ResourceCardRequest) game.getLeaderCards().get(15).getRequest();
		assertEquals(3, request.getBuildingCards().intValue());
		assertEquals(0, request.getCharacterCards().intValue());
		assertEquals(0, request.getTerritoryCards().intValue());
		assertEquals(0, request.getVentureCards().intValue());
		assertNotNull(request.getResource());
		assertEquals("You must have%n- 3 Buildings cards%n- coins: 12%nfaith: 2%n", request.getInfo());
	}
}
