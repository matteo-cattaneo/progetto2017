package it.polimi.ingsw.LM22.model;

import org.junit.Test;

import junit.framework.TestCase;

public class TestCardSpace extends TestCase{
	CardSpace prova;
	Player p;
	FamilyMember fm;

	// assigning the values
	public void setUp() {
		prova = new CardSpace();
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
	public void testFamilYMember(){
		/**
		 * FamilyMember test
		 */
		prova.setMember(fm);
		assertEquals(fm, prova.getMember());
				
	}
}
