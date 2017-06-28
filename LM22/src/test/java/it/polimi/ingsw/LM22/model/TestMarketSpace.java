package it.polimi.ingsw.LM22.model;

import org.junit.Test;

import junit.framework.TestCase;

public class TestMarketSpace extends TestCase{
	MarketSpace prova;
	Player p;
	FamilyMember fm;

	// assigning the values
	public void setUp() {
		prova = new MarketSpace();
		p = new Player("Name", "Blue");
		fm = new FamilyMember(p, "Orange");
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
	public void testMember(){
		/**
		 * FamilyMember test
		 */
		prova.setMember(fm);
		assertEquals(fm, prova.getMember());			
	}
	
}
