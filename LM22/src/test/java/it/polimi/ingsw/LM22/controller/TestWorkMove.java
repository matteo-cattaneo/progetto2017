package it.polimi.ingsw.LM22.controller;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import junit.framework.TestCase;

public class TestWorkMove extends TestCase {
	WorkMove workMove;

	@Test
	public void testWorkMove() {
		Player p = new Player("name", "color");
		FamilyMember fm = new FamilyMember(p, "color");
		Resource serv = new Resource(0, 0, 1, 0, 0, 0, 0);
		workMove = new WorkMove(p, fm, serv, "Production");
		assertEquals(p, workMove.getPlayer());
		assertEquals(fm, workMove.getMemberUsed());
		assertEquals(serv, workMove.getServantsAdded());
		assertEquals("Production", workMove.getWorkType());
	}
}
