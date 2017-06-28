package it.polimi.ingsw.LM22.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

public class TestTower extends TestCase{

	Tower prova;
	
	public void setUp(){
		prova = new Tower();
	}
	
	@Test
	public void testOccupied(){
		assertEquals(false, prova.isOccupied());
		prova.setOccupied(true);
		assertEquals(true, prova.isOccupied());
	}
	
	@Test 
	public void testColoredMemberOnIt(){
		/**
		 * ColoredMemberList test
		 */
		List<String> colors = new ArrayList<String>();
		colors.add("Green");
		colors.add("Yellow");
		prova.setColoredMembersOnIt(colors);
		assertEquals(colors, prova.getColoredMembersOnIt());
		colors.add("Red");
		prova.getColoredMembersOnIt().add("Red");
		assertEquals(colors, prova.getColoredMembersOnIt());
	}
	
	
}
