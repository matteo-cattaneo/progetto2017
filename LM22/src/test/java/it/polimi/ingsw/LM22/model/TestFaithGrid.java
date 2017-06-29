package it.polimi.ingsw.LM22.model;

import org.junit.Test;

import junit.framework.TestCase;

public class TestFaithGrid extends TestCase {

	FaithGrid prova;

	public void setUp() {
		prova = new FaithGrid();
		
	}
	
	@Test
	public void testRewards(){
		Resource[] rewards = { new Resource(0, 0, 0, 0, 0, 0, 0), new Resource(0, 0, 0, 0, 1, 0, 0),
				new Resource(0, 0, 0, 0, 2, 0, 0), new Resource(0, 0, 0, 0, 3, 0, 0), new Resource(0, 0, 0, 0, 4, 0, 0),
				new Resource(0, 0, 0, 0, 5, 0, 0), new Resource(0, 0, 0, 0, 7, 0, 0), new Resource(0, 0, 0, 0, 9, 0, 0),
				new Resource(0, 0, 0, 0, 11, 0, 0), new Resource(0, 0, 0, 0, 13, 0, 0),
				new Resource(0, 0, 0, 0, 15, 0, 0), new Resource(0, 0, 0, 0, 17, 0, 0),
				new Resource(0, 0, 0, 0, 19, 0, 0), new Resource(0, 0, 0, 0, 22, 0, 0),
				new Resource(0, 0, 0, 0, 25, 0, 0), new Resource(0, 0, 0, 0, 30, 0, 0) };
		prova.setRewards(rewards);
		assertEquals(rewards, prova.getRewards());
		Resource result = prova.getReward(5);
		assertEquals(result, prova.getReward(5));
	}
	
	@Test
	public void testGridLength(){
		assertEquals(16, prova.getGRIDLENGTH().intValue());
	}
}
