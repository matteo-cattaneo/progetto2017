package it.polimi.ingsw.LM22.controller;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import junit.framework.TestCase;

public class TestCardMove extends TestCase {
	CardMove cardMove;

	@Test
	public void testCardMove() {
		Player p = new Player("name", "color");
		FamilyMember fm = new FamilyMember(p, "color");
		Resource serv = new Resource(0, 0, 1, 0, 0, 0, 0);
		cardMove = new CardMove(p, fm, serv, 0, 0);
		assertEquals(p, cardMove.getPlayer());
		assertEquals(fm, cardMove.getMemberUsed());
		assertEquals(serv, cardMove.getServantsAdded());
		assertEquals(0, cardMove.getLevelSelected().intValue());
		assertEquals(0, cardMove.getTowerSelected().intValue());
	}
}
