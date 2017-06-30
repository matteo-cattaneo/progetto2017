package it.polimi.ingsw.LM22.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.LM22.controller.FileParser;
import junit.framework.TestCase;

public class TestPersonalBoard extends TestCase {
	PersonalBoard prova;
	FileParser fp;
	Game game;

	public void setUp() {
		prova = new PersonalBoard();
		fp = new FileParser();
		game = new Game();
	}

	@Test
	public void testResource() {
		Resource test = new Resource(1, 2, 3, 4, 5, 6, 7);
		prova.setResources(test.copy());
		assertEquals(test.getWood().intValue(), prova.getResources().getWood().intValue());
		assertEquals(test.getStone().intValue(), prova.getResources().getStone().intValue());
		assertEquals(test.getServants().intValue(), prova.getResources().getServants().intValue());
		assertEquals(test.getCoins().intValue(), prova.getResources().getCoins().intValue());
		assertEquals(test.getFaith().intValue(), prova.getResources().getFaith().intValue());
		assertEquals(test.getMilitary().intValue(), prova.getResources().getMilitary().intValue());
		assertEquals(test.getVictory().intValue(), prova.getResources().getVictory().intValue());
	}

	@Test
	public void testBonusBoard() {
		PersonalBonusTile pbt = new PersonalBonusTile();
		prova.setBonusBoard(pbt);
		assertEquals(pbt, prova.getBonusBoard());
	}

	@Test
	public void testTerritoryCards() {
		List<TerritoryCard> list = new ArrayList<TerritoryCard>();
		TerritoryCard card1 = new TerritoryCard();
		TerritoryCard card2 = new TerritoryCard();
		assertEquals(true, prova.getTerritoriesCards().isEmpty());
		list.add(card1);
		list.add(card2);
		prova.setTerritoriesCards(list);
		assertEquals(list, prova.getTerritoriesCards());
	}

	@Test
	public void testCharacterCard() {
		List<CharacterCard> list = new ArrayList<CharacterCard>();
		CharacterCard card1 = new CharacterCard();
		CharacterCard card2 = new CharacterCard();
		assertEquals(true, prova.getTerritoriesCards().isEmpty());
		list.add(card1);
		list.add(card2);
		prova.setCharactersCards(list);
		assertEquals(list, prova.getCharactersCards());
	}

	@Test
	public void testBuildingCard() {
		List<BuildingCard> list = new ArrayList<BuildingCard>();
		BuildingCard card1 = new BuildingCard();
		BuildingCard card2 = new BuildingCard();
		assertEquals(true, prova.getBuildingsCards().isEmpty());
		list.add(card1);
		list.add(card2);
		prova.setBuildingsCards(list);
		assertEquals(list, prova.getBuildingsCards());
	}

	@Test
	public void testVentureCard() {
		List<VentureCard> list = new ArrayList<VentureCard>();
		VentureCard card1 = new VentureCard();
		VentureCard card2 = new VentureCard();
		assertEquals(true, prova.getVenturesCards().isEmpty());
		list.add(card1);
		list.add(card2);
		prova.setVenturesCards(list);
		assertEquals(list, prova.getVenturesCards());
	}

	@Test
	public void testPersonalBonusTile() throws IOException {
		fp.getPersonalBonusTile(game);
		assertEquals(1, game.getPersonalBonusTile()[0].getRequirement().intValue());
		assertNotNull(game.getPersonalBonusTile()[0].getHarvestEffect());
		assertNotNull(game.getPersonalBonusTile()[0].getProductionEffect());
	}
}
