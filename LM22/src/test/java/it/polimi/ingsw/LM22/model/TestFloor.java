package it.polimi.ingsw.LM22.model;

import org.junit.Test;

import junit.framework.TestCase;

public class TestFloor extends TestCase{

	Floor f;
	CardSpace cs;
	DevelopmentCard tc;
	DevelopmentCard cc;
	DevelopmentCard bc;
	DevelopmentCard vc;
	
	public void setUp(){
		f = new Floor();
		cs = new CardSpace();
		tc = new TerritoryCard();
		cc = new CharacterCard();
		bc = new BuildingCard();
		vc = new VentureCard();
	}
	
	@Test
	public void testSpace(){
		f.setSpace(cs);
		assertEquals(cs, f.getSpace());
	}
	
	@Test
	public void testCard(){
		f.setCard(tc);
		assertEquals(tc, f.getCard());
		f.setCard(cc);
		assertEquals(cc, f.getCard());
		f.setCard(bc);
		assertEquals(bc, f.getCard());
		f.setCard(vc);
		assertEquals(vc, f.getCard());
	}
}
