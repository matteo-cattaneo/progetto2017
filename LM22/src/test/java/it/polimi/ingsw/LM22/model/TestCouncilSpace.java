package it.polimi.ingsw.LM22.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.LM22.controller.FileParser;
import junit.framework.TestCase;

public class TestCouncilSpace extends TestCase {

	CouncilSpace prova;
	Player p1;
	Player p2;
	FamilyMember fm1;
	FamilyMember fm2;
	FileParser fp;
	Game game;

	// assigning the values
	public void setUp() {
		prova = new CouncilSpace();
		p1 = new Player("Name1", "Blue");
		p2 = new Player("Name2", "Green");
		fm1 = new FamilyMember(p1, "Orange");
		fm2 = new FamilyMember(p2, "Uncolored");
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
	public void testFamilyMemberList() {
		/**
		 * FamilyMember list test
		 */
		assertEquals(true, prova.getMembers().isEmpty());
		List<FamilyMember> list = new ArrayList<FamilyMember>();
		list.add(fm1);
		assertEquals(1, list.size());
		list.add(fm2);
		prova.setMembers(list);
		assertEquals(list, prova.getMembers());
		assertEquals(true, prova.getMembers().containsAll(list));
	}

	@Test
	public void testCouncilSpace() throws IOException {
		fp.getCouncilSpace(game);
		assertNotNull(game.getBoardgame().getCouncilPalace());
		assertEquals(1, game.getBoardgame().getCouncilPalace().getSpaceRequirement().intValue());
		assertEquals(1, game.getBoardgame().getCouncilPalace().getCouncilPrivilege().intValue());
		assertNotNull(game.getBoardgame().getCouncilPalace().getReward());
	}
}
