package it.polimi.ingsw.LM22.model;

import org.junit.Test;

import junit.framework.TestCase;

public class TestResource extends TestCase {
	Resource prova;

	// assigning the values
	public void setUp() {
		prova = new Resource(0, 2, 3, 4, 8, 1, 4);
	}

	@Test
	public void testResource() {
		/*
		 * getInfo() testing
		 */
		String info = prova.getInfo();
		//testa anche che non si stampi un attributo se <=0
		assertEquals("stone: 2%nservants: 3%ncoins: 4%nfaith: 8%nmilitary: 1%nvictory: 4%n", info);
		/*assertEquals(copia, prova);*/
		
		/*
		 * Getters' test
		 */
		int a = prova.getWood();
		assertEquals(0, a);
		int b = prova.getStone();
		assertEquals(2, b);
		int c = prova.getServants();
		assertEquals(3, c);
		int d = prova.getCoins();
		assertEquals(4, d);
		int e = prova.getFaith();
		assertEquals(8, e);
		int f = prova.getMilitary();
		assertEquals(1, f);
		int g = prova.getVictory();
		assertEquals(4, g);

		/*
		 * clone() test
		 */
		Resource copia=prova.clone();
		int a1 = copia.getWood();
		assertEquals(a1, a);
		int b1 = copia.getStone();
		assertEquals(b1, b);
		int c1 = copia.getServants();
		assertEquals(c1, c);
		int d1 = copia.getCoins();
		assertEquals(d1, d);
		int e1 = copia.getFaith();
		assertEquals(e1, e);
		int f1 = copia.getMilitary();
		assertEquals(f1, f);
		int g1 = copia.getVictory();
		assertEquals(g1, g);
		
		/*
		 * Setters' test
		 */
		prova.setWood(6);
		a = prova.getWood();
		assertEquals(6, a);
		prova.setStone(1);
		b = prova.getStone();
		assertEquals(1, b);
		prova.setServants(11);
		c = prova.getServants();
		assertEquals(11, c);
		prova.setCoins(5);
		d = prova.getCoins();
		assertEquals(5, d);
		prova.setFaith(0);
		e = prova.getFaith();
		assertEquals(0, e);
		prova.setMilitary(6);
		f = prova.getMilitary();
		assertEquals(6, f);
		prova.setVictory(7);
		g = prova.getVictory();
		assertEquals(7, g);
	}

}
