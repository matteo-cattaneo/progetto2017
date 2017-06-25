package it.polimi.ingsw.LM22.controller;

import org.junit.Test;
import it.polimi.ingsw.LM22.model.Resource;
import junit.framework.TestCase;

public class TestDiffResources extends TestCase {
	Resource firstResource, secondResource, result;

	// assigning the values
	public void setUp() {
		firstResource = new Resource(0, 2, 0, 5, 9, 2, 3);
		secondResource = new Resource(1, 2, 0, 4, 0, 1, 4);
	}

	@Test
	public void testDiffResource() {
		ResourceHandler rh = new ResourceHandler();
		result = rh.diffResource(firstResource, secondResource);
		assertEquals(-1, result.getWood().intValue());
		assertEquals(0, result.getStone().intValue());
		assertEquals(0, result.getServants().intValue());
		assertEquals(1, result.getCoins().intValue());
		assertEquals(9, result.getFaith().intValue());
		assertEquals(1, result.getMilitary().intValue());
		assertEquals(-1, result.getVictory().intValue());
	}
}
