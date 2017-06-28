package it.polimi.ingsw.LM22.model;

import java.util.HashMap;

import org.junit.Test;

import junit.framework.TestCase;

public class TestBoardGame extends TestCase{

	BoardGame prova;
	
	public void setUp(){
		prova = new BoardGame();
	}
	
	@Test
	public void testTowers(){
		Tower[] t = new Tower[4];
		prova.setTowers(t);
		assertEquals(t, prova.getTowers());
	}
	
	@Test
	public void testMarket(){
		MarketSpace[] m = new MarketSpace[2];
		prova.setMarket(m);
		assertEquals(m, prova.getMarket());
	}
	
	@Test
	public void testFaithGrid(){
		FaithGrid f = new FaithGrid();
		prova.setFaithGrid(f);
		assertEquals(f, prova.getFaithGrid());
	}
	
	@Test
	public void testWorkSpace(){
		WorkSpace ps = new WorkSpace();
		WorkSpace hs = new WorkSpace();
		prova.setProductionSpace(ps);
		prova.setHarvestSpace(hs);
		assertEquals(ps, prova.getProductionSpace());
		assertEquals(hs, prova.getHarvestSpace());
		assertEquals(ps, prova.getWorkSpace("PRODUCTION"));
		assertEquals(hs, prova.getWorkSpace("HARVEST"));
	}
	
	@Test
	public void testCouncilSpace(){
		CouncilSpace cs = new CouncilSpace();
		prova.setCouncilPalace(cs);
		assertEquals(cs, prova.getCouncilPalace());
	}
	
	@Test
	public void testDice(){
		HashMap <String, Integer> dices = new HashMap<String, Integer>();
		dices.put("Orange", 2);
		dices.put("White", 6);
		dices.put("Uncolored", 1);
		dices.put("Black", 5);
		prova.setDices(dices);
		prova.setDice("Orange", 5);
		assertEquals(5, prova.getDice("Orange").intValue());
		prova.setDice("White", 0);
		assertEquals(0, prova.getDice("White").intValue());
		prova.setDice("Uncolored", 3);
		assertEquals(3, prova.getDice("Uncolored").intValue());
		prova.setDice("Black", 4);
		assertEquals(4, prova.getDice("Black").intValue());
	}
	
}
