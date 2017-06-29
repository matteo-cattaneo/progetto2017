package it.polimi.ingsw.LM22.model;

import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

public class TestCardSpace extends TestCase {
	CardSpace prova;
	Player p;
	FamilyMember fm;
	FileParser fp;
	Game game;

	// assigning the values
	public void setUp() {
		prova = new CardSpace();
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
	public void testFamilYMember() {
		/**
		 * FamilyMember test
		 */
		prova.setMember(fm);
		assertEquals(fm, prova.getMember());

	}

	@Test
	public void testCardSpace() throws IOException {
		fp.getCardSpace(game);
		assertNotNull(game.getBoardgame().getTowers());
		assertFalse(game.getBoardgame().getTowers()[0].isOccupied());
		assertNotNull(game.getBoardgame().getTowers()[0].getColoredMembersOnIt());
		assertEquals(1, game.getBoardgame().getTowers()[0].getFloor()[0].getSpace().getSpaceRequirement().intValue());
		assertNotNull(game.getBoardgame().getTowers()[0].getFloor()[0].getSpace().getReward());
	}

}
