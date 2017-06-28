package it.polimi.ingsw.LM22.controller;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.Player;
import junit.framework.TestCase;

public class TestEndMove extends TestCase {
	EndMove endMove;

	@Test
	public void testEndMove() {
		Player p = new Player("name", "color");
		endMove = new EndMove(p, "error");
		assertEquals(p, endMove.getPlayer());
		assertEquals("error", endMove.getError());
	}
}
