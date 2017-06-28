package it.polimi.ingsw.LM22.model;

import org.junit.Test;

import junit.framework.TestCase;

public class TestFamilyMember extends TestCase{

	FamilyMember prova;
	Player p;
	
	public void setUp(){
		p = new Player ("Name", "Green");
		prova = new FamilyMember(p, "Orange");
	}
	
	@Test
	public void testFamilyMemberConstructor(){
		assertEquals(false, prova.isUsed().booleanValue());
		assertEquals(p, prova.getPlayer());
		assertEquals("Orange", prova.getColor());
	}
	
	@Test 
	public void testUsed(){
		prova.setUsed(true);
		assertEquals(true, prova.isUsed().booleanValue());
	}

	@Test
	public void testValue(){
		prova.setValue(4);
		assertEquals(4, prova.getValue().intValue());
	}
	
}
