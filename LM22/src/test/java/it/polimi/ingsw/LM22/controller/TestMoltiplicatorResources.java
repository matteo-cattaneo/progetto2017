package it.polimi.ingsw.LM22.controller;

import org.junit.Test;
import it.polimi.ingsw.LM22.model.Resource;
import junit.framework.TestCase;

public class TestMoltiplicatorResources extends TestCase {
	Resource resource, result;
	Integer molt;

	// assigning the values
	public void setUp() {
		resource = new Resource(0, 2, 0, 5, 9, 2, 3);
		molt = 2;
	}

	@Test
	public void testMoltiplicatorResource() {
		ResourceHandler rh = new ResourceHandler();
		result = rh.resourceMultiplication(resource, molt);
		assertEquals(0, result.getWood().intValue());
		assertEquals(4, result.getStone().intValue());
		assertEquals(0, result.getServants().intValue());
		assertEquals(10, result.getCoins().intValue());
		assertEquals(18, result.getFaith().intValue());
		assertEquals(4, result.getMilitary().intValue());
		assertEquals(6, result.getVictory().intValue());
	}
}
