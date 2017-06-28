package it.polimi.ingsw.LM22.controller;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import junit.framework.TestCase;

public class TestCouncilMove extends TestCase {
	CouncilMove councilMove;

	@Test
	public void testCouncilMove() {
		Player p = new Player("name", "color");
		FamilyMember fm = new FamilyMember(p, "color");
		Resource serv = new Resource(0, 0, 1, 0, 0, 0, 0);
		councilMove = new CouncilMove(p, fm, serv);
		assertEquals(p, councilMove.getPlayer());
		assertEquals(fm, councilMove.getMemberUsed());
		assertEquals(serv, councilMove.getServantsAdded());
	}
}
