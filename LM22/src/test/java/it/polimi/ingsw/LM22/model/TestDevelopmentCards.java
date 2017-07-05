package it.polimi.ingsw.LM22.model;

import java.io.IOException;

import org.junit.Test;

import it.polimi.ingsw.LM22.controller.FileParser;
import junit.framework.TestCase;

public class TestDevelopmentCards extends TestCase {
	FileParser fp;
	Game game;

	// assigning the values
	public void setUp() throws IOException {
		fp = new FileParser();
		game = new Game();
		fp.getDevCards(game);
	}

	@Test
	public void testBuildingCards() {
		assertEquals(24, game.getBuildingCards().size());
		BuildingCard building = game.getBuildingCards().get(1);
		assertEquals("Falegnameria", building.getName());
		assertEquals(1, building.getPeriod().intValue());
		assertNotNull(building.getCost());
		assertEquals(4, building.getRequirement().intValue());
		assertEquals(0, ((ResourcePrivilegeEffect) building.getImmediateEffect()).getCouncilPrivilege().intValue());
		assertNotNull(((ResourcePrivilegeEffect) building.getImmediateEffect()).getResource());
		assertNotNull(building.getPermanentEffect());
		assertNotNull(((DoubleChangeEffect) building.getPermanentEffect()).getExchangeEffect1());
		assertNotNull(((DoubleChangeEffect) building.getPermanentEffect()).getExchangeEffect2());
		assertEquals("Name: Falegnameria%nPeriod: 1%nCard cost:%nwood: 2%ncoins: 1%n"
				+ "Immediate effect:%nYou earn%nvictory: 3%nPermament effect:%n"
				+ "You can exchange wood: 1%n to coins: 3%n OR You can exchange wood: 2%n to coins: 5%n"
				+ "Card type: Building%n", building.getInfo());
	}

	@Test
	public void testCharacterCards() {
		assertEquals(24, game.getCharacterCards().size());
		CharacterCard character = game.getCharacterCards().get(1);
		assertEquals("Badessa", character.getName());
		assertEquals(1, character.getPeriod().intValue());
		assertNotNull(character.getCost());
		assertNotNull(((CardActionEffect) character.getImmediateEffect()).getCardDiscount());
		assertEquals(-1, ((CardActionEffect) character.getImmediateEffect()).getCardType().intValue());
		assertEquals(0, ((CardActionEffect) character.getImmediateEffect()).getCouncilPrivilege().intValue());
		assertEquals(4, ((CardActionEffect) character.getImmediateEffect()).getDiceValue().intValue());
		assertNotNull(((CardActionEffect) character.getImmediateEffect()).getResource());
		assertNotNull(character.getImmediateEffect());
		assertNotNull(character.getPermanentEffect());
		assertEquals("Name: Badessa%nPeriod: 1%nCard cost:%ncoins: 3%n"
				+ "Immediate effect:%nYou can get a card on the tower you choose%nThe action has a value of 4%n"
				+ "You get a card discount: No resource%nYou get a reward: faith: 1%n"
				+ "Permament effect:%nNo Effect%nCard type: Character%n", character.getInfo());
	}

	@Test
	public void testTerritoryCards() {
		assertEquals(24, game.getTerritoryCards().size());
		TerritoryCard territory = game.getTerritoryCards().get(0);
		assertEquals("Rocca", territory.getName());
		assertEquals(1, territory.getPeriod().intValue());
		assertEquals(5, territory.getRequirement().intValue());
		assertNotNull(territory.getImmediateEffect());
		assertNotNull(territory.getPermanentEffect());
		assertNotNull(((ResourcePrivilegeEffect) territory.getPermanentEffect()).getResource());
		assertEquals("Name: Rocca%nPeriod: 1%nImmediate effect:%nNo Effect%n"
				+ "Permament effect:%nRequirement: 5%nYou earn%nstone: 1%nmilitary: 2%n" + "Card type: Territory%n",
				territory.getInfo());
	}

	@Test
	public void testVentureCards() {
		// resource cost Venture card
		assertEquals(24, game.getVentureCards().size());
		VentureCard venture = game.getVentureCards().get(23);
		assertEquals("Migliorare le Strade", venture.getName());
		assertEquals(3, venture.getPeriod().intValue());
		assertNotNull(venture.getCardCost1());
		assertNotNull(venture.getCardCost2());
		assertNotNull(venture.getImmediateEffect());
		assertEquals("PRODUCTION", ((WorkActionEffect) venture.getImmediateEffect()).getTypeOfWork());
		assertEquals(3, ((WorkActionEffect) venture.getImmediateEffect()).getWorkActionValue().intValue());
		assertEquals(0, ((WorkActionEffect) venture.getImmediateEffect()).getCouncilPrivilege().intValue());
		assertNotNull(((WorkActionEffect) venture.getImmediateEffect()).getResource());
		assertNotNull(venture.getPermanentEffect());
		assertEquals("Name: Migliorare le Strade%nPeriod: 3%nCard cost:%nservants: 3%ncoins: 4%n"
				+ "Immediate effect:%nYou earn No resource%nYou can do a PRODUCTION Action with a value of 3%n"
				+ "Permament effect:%nvictory: 5%nCard type: Venture%n", venture.getInfo());
		// military cost Venture card
		venture = game.getVentureCards().get(1);
		assertEquals("Combattere le Eresie", venture.getName());
		assertEquals(1, venture.getPeriod().intValue());
		assertNotNull(venture.getCardCost1());
		assertNotNull(venture.getCardCost2());
		assertNotNull(venture.getImmediateEffect());
		assertNotNull(venture.getPermanentEffect());
		assertEquals("Name: Combattere le Eresie%nPeriod: 1%nCard cost:%nRequire:%nmilitary: 5%n"
				+ "Cost:%nmilitary: 3%nImmediate effect:%nYou earn%nfaith: 2%nPermament effect:%nvictory: 5%n"
				+ "Card type: Venture%n", venture.getInfo());
	}
}
