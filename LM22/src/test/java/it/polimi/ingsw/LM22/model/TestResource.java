package it.polimi.ingsw.LM22.model;

import org.junit.Test;

import junit.framework.TestCase;

public class TestResource extends TestCase {
	Resource prova;
	Resource nothing;
	String info;
	
	// assigning the values
	public void setUp() {
		prova = new Resource(1, 2, 3, 4, 8, 1, 4);
		nothing = new Resource(0,0,0,0,0,0,0);
	}

	@Test
	public void testResource() {
		/**
		 * getInfo() testing
		 */
		
		info = prova.getInfo();
		//testa anche che non si stampi un attributo se <=0
		assertEquals("wood: 1%nstone: 2%nservants: 3%ncoins: 4%nfaith: 8%nmilitary: 1%nvictory: 4%n", info);
		/*assertEquals(copia, prova);*/
		info = nothing.getInfo();
		assertEquals("No resource%n", info);
		
		
		/**
		 * Getters' test
		 */
		assertEquals(1, prova.getWood().intValue());
		assertEquals(2, prova.getStone().intValue());
		assertEquals(3, prova.getServants().intValue());
		assertEquals(4, prova.getCoins().intValue());
		assertEquals(8, prova.getFaith().intValue());
		assertEquals(1, prova.getMilitary().intValue());
		assertEquals(4, prova.getVictory().intValue());

		/**
		 * clone() test
		 */
		Resource copia=prova.clone();
		assertEquals(prova.getWood(), copia.getWood());
		assertEquals(prova.getStone(), copia.getStone());
		assertEquals(prova.getServants(), copia.getServants()); 
		assertEquals(prova.getCoins(), copia.getCoins());
		assertEquals(prova.getFaith(), copia.getFaith()); 
		assertEquals(prova.getMilitary(), copia.getMilitary()); 
		assertEquals(prova.getVictory(), copia.getVictory());
		
		/**
		 * Setters' test
		 */
		prova.setWood(6);
		assertEquals(6, prova.getWood().intValue());
		prova.setStone(1);
		assertEquals(1, prova.getStone().intValue());
		prova.setServants(11);
		assertEquals(11, prova.getServants().intValue());
		prova.setCoins(5);
		assertEquals(5, prova.getCoins().intValue());
		prova.setFaith(0);
		assertEquals(0, prova.getFaith().intValue());
		prova.setMilitary(6);
		assertEquals(6, prova.getMilitary().intValue());
		prova.setVictory(7);
		assertEquals(7, prova.getVictory().intValue());
	}
}
