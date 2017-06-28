package it.polimi.ingsw.LM22.controller;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import junit.framework.TestCase;

public class TestMarketMove extends TestCase {
	MarketMove marketMove;

	@Test
	public void testMarketMove() {
		Player p = new Player("name", "color");
		FamilyMember fm = new FamilyMember(p, "color");
		Resource serv = new Resource(0, 0, 1, 0, 0, 0, 0);
		marketMove = new MarketMove(p, fm, serv, 0);
		assertEquals(p, marketMove.getPlayer());
		assertEquals(fm, marketMove.getMemberUsed());
		assertEquals(serv, marketMove.getServantsAdded());
		assertEquals(0, marketMove.getMarketSpaceSelected().intValue());
	}
}
