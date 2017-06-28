package it.polimi.ingsw.LM22.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

public class TestWorkSpace extends TestCase{

	WorkSpace prova;
	Player p1;
	Player p2;
	FamilyMember fm1;
	FamilyMember fm2;

	// assigning the values
	public void setUp() {
		prova = new WorkSpace();
		p1 = new Player("Name1", "Blue");
		p2 = new Player("Name2", "Green");
		fm1 = new FamilyMember(p1, "Orange");
		fm2 = new FamilyMember(p2, "Uncolored");
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
	public void testFamilyMemberList(){
		/**
		 * FamilyMember list test
		 */
		assertEquals(true, prova.getMembers().isEmpty());
		List<FamilyMember> list = new ArrayList<FamilyMember>();
		list.add(fm1);
		assertEquals(1, list.size());
		list.add(fm2);
		prova.getMembers().addAll(list);
		assertEquals(list, prova.getMembers());
		assertEquals(true, prova.getMembers().containsAll(list));
	}
	
	@Test 
	public void testColoredMemberOnIt(){
		/**
		 * ColoredMemberList test
		 */
		List<String> colors = new ArrayList<String>();
		colors.add(p1.getColor());
		colors.add(p2.getColor());
		colors.add("Yellow");
		prova.setColoredMemberOnIt(colors);
		assertEquals(colors, prova.getColoredMemberOnIt());
		colors.add("Red");
		prova.getColoredMemberOnIt().add("Red");
		assertEquals(colors, prova.getColoredMemberOnIt());	
	}
	
	@Test 
	public void testWorkType(){
		prova.setWorkType("PRODUCTION");
		assertEquals("PRODUCTION", prova.getWorkType());
		prova.setWorkType("HARVEST");
		assertEquals("HARVEST", prova.getWorkType());
		
	}
	
}
