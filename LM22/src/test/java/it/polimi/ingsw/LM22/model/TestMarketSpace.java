package it.polimi.ingsw.LM22.model;

import java.io.IOException;

import org.junit.Test;

import it.polimi.ingsw.LM22.controller.FileParser;
import junit.framework.TestCase;

public class TestMarketSpace extends TestCase {
	MarketSpace prova;
	Player p;
	FamilyMember fm;
	FileParser fp;
	Game game;

	// assigning the values
	public void setUp() {
		prova = new MarketSpace();
		p = new Player("Name", "Blue");
		fm = new FamilyMember(p, "Orange");
		fp = new FileParser();
		game = new Game();
	}

	@Test
	public void testSpaceRequirement() {
		/**
		 * Requirement Test
		 */
		prova.setSpaceRequirement(1);
		assertEquals(1, prova.getSpaceRequirement().intValue());
	}

	@Test
	public void testMember() {
		/**
		 * FamilyMember test
		 */
		prova.setMember(fm);
		assertEquals(fm, prova.getMember());
	}

	@Test
	public void testMarketSpace() throws IOException {
		fp.getMarketSpace(game);
		assertNotNull(game.getBoardgame().getMarket());
		assertEquals(0, game.getBoardgame().getMarket()[0].getCouncilPrivilege().intValue());
		assertEquals(1, game.getBoardgame().getMarket()[0].getSpaceRequirement().intValue());
		assertNotNull(game.getBoardgame().getMarket()[0].getReward());
	}
}
