package it.polimi.ingsw.LM22.controller;

import org.junit.Test;
import it.polimi.ingsw.LM22.model.Resource;
import junit.framework.TestCase;

public class TestSumResources extends TestCase {
	Resource firstResource, secondResource, result;

	// assigning the values
	public void setUp() {
		firstResource = new Resource(0, 2, 0, 5, 9, 2, 3);
		secondResource = new Resource(1, 2, 0, 4, 0, 1, 4);
	}

	@Test
	public void testSumResource() {
		ResourceHandler rh = new ResourceHandler();
		result = rh.sumResource(firstResource, secondResource);
		assertEquals(1, result.getWood().intValue());
		assertEquals(4, result.getStone().intValue());
		assertEquals(0, result.getServants().intValue());
		assertEquals(9, result.getCoins().intValue());
		assertEquals(9, result.getFaith().intValue());
		assertEquals(3, result.getMilitary().intValue());
		assertEquals(7, result.getVictory().intValue());
	}
}
