package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.NoCardSpaceBonusEffect;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.excommunication.DoubleServantsEx;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;
import junit.framework.TestCase;

public class TestMoveManager extends TestCase {

	private static final Logger LOGGER = Logger.getLogger(MoveManager.class.getClass().getSimpleName());
	InitialConfigurator init;
	Game game;
	PlayerInfo pi1;
	PlayerInfo pi2;
	PlayerInfo pi3;
	PlayerInfo pi4;
	final String[] colors = { "Orange", "Black", "White" };
	ResourceHandler r;
	MainGameController mainGC;
	MoveManager prova;
	EffectManager effectManager;
	FileParser f;

	public void setUp() throws RemoteException {
		ArrayList<PlayerInfo> pinfolist = new ArrayList<PlayerInfo>();
		pi1 = new PlayerInfo();
		pi2 = new PlayerInfo();
		pi3 = new PlayerInfo();
		pi4 = new PlayerInfo();
		pi1.setName("Nicola");
		pi1.setName("Matteo");
		pi1.setName("Esempio");
		pi1.setName("Esempio1");
		pinfolist.add(pi1);
		pinfolist.add(pi2);
		pinfolist.add(pi3);
		pinfolist.add(pi4);
		r = new ResourceHandler();
		mainGC = new MainGameController(pinfolist);
		game = mainGC.getGame();
		prova = new MoveManager(game, mainGC);
		effectManager = new EffectManager(prova);
		init = new InitialConfigurator(pinfolist, r, effectManager, mainGC);
		f = new FileParser();
	}

	@Test
	public void testFirstTerritoryMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		test.getEffects().add(new DoubleServantsEx());
		test.getEffects().add(new NoCardSpaceBonusEffect());
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().size() == 1);
		assertTrue(test.getMembers().get(0).isUsed());
		assertTrue(game.getBoardgame().getTowers()[0].isOccupied());
		assertNotNull(game.getBoardgame().getTowers()[0].getFloor()[0].getSpace().getMember());
		assertTrue(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 8);
	}
	
	@Test
	public void testMoreTerritoryMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		assertEquals(6, test.getPersonalBoard().getTerritoriesCards().size());
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Territory Max Reached!");
		}
		assertEquals(6, test.getPersonalBoard().getTerritoriesCards().size());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(game.getBoardgame().getTowers()[0].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(5, test.getPersonalBoard().getResources().getCoins().intValue());
	}

	@Test
	public void testFirstCharacterMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getCharactersCards().isEmpty());
		game.getBoardgame().getTowers()[1].getFloor()[0].setCard(game.getCharacterCards().get(0));
		assertTrue(game.getCharacterCards().get(0).getName().equals("Predicatore"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 2, 0, 0, 0, 0), 1, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getCharactersCards().size() == 1);
		assertTrue(test.getMembers().get(0).isUsed());
		assertTrue(game.getBoardgame().getTowers()[1].isOccupied());
		assertNotNull(game.getBoardgame().getTowers()[1].getFloor()[0].getSpace().getMember());
		assertTrue(game.getBoardgame().getTowers()[1].getColoredMembersOnIt().contains(test.getColor()));
		assertTrue(test.getPersonalBoard().getResources().getServants() == 1);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 3);
		assertTrue(test.getPersonalBoard().getResources().getFaith() == 4);
		assertTrue(test.getEffects().contains(game.getCharacterCards().get(0).getPermanentEffect()));
	}
	
	@Test
	public void testMoreCharacterMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getCharactersCards().isEmpty());
		test.getPersonalBoard().getCharactersCards().add(new CharacterCard());
		test.getPersonalBoard().getCharactersCards().add(new CharacterCard());
		test.getPersonalBoard().getCharactersCards().add(new CharacterCard());
		test.getPersonalBoard().getCharactersCards().add(new CharacterCard());
		test.getPersonalBoard().getCharactersCards().add(new CharacterCard());
		test.getPersonalBoard().getCharactersCards().add(new CharacterCard());
		game.getBoardgame().getTowers()[1].getFloor()[0].setCard(game.getCharacterCards().get(0));
		assertTrue(game.getCharacterCards().get(0).getName().equals("Predicatore"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 2, 0, 0, 0, 0), 1, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Character Max Reached!");
		}
		assertEquals(6, test.getPersonalBoard().getCharactersCards().size());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(game.getBoardgame().getTowers()[1].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[1].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(3, test.getPersonalBoard().getResources().getServants().intValue());
		assertEquals(5, test.getPersonalBoard().getResources().getCoins().intValue());
		assertEquals(0, test.getPersonalBoard().getResources().getFaith().intValue());
		assertFalse(test.getEffects().contains(game.getCharacterCards().get(0).getPermanentEffect()));
	}

	@Test
	public void testFirstBuildingMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getBuildingsCards().isEmpty());
		game.getBoardgame().getTowers()[2].getFloor()[0].setCard(game.getBuildingCards().get(0));
		assertTrue(game.getBuildingCards().get(0).getName().equals("Cappella"));
		CardMove move = new CardMove(test, test.getMembers().get(3), new Resource(0, 0, 1, 0, 0, 0, 0), 2, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getBuildingsCards().size() == 1);
		assertTrue(test.getMembers().get(3).isUsed());
		assertTrue(game.getBoardgame().getTowers()[2].isOccupied());
		assertNotNull(game.getBoardgame().getTowers()[2].getFloor()[0].getSpace().getMember());
		assertFalse(game.getBoardgame().getTowers()[2].getColoredMembersOnIt().contains(test.getColor()));
		assertTrue(test.getPersonalBoard().getResources().getWood() == 0);
		assertTrue(test.getPersonalBoard().getResources().getFaith() == 1);
	}
	
	@Test
	public void testMoreBuildingMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getBuildingsCards().isEmpty());
		test.getPersonalBoard().getBuildingsCards().add(new BuildingCard());
		test.getPersonalBoard().getBuildingsCards().add(new BuildingCard());
		test.getPersonalBoard().getBuildingsCards().add(new BuildingCard());
		test.getPersonalBoard().getBuildingsCards().add(new BuildingCard());
		test.getPersonalBoard().getBuildingsCards().add(new BuildingCard());
		test.getPersonalBoard().getBuildingsCards().add(new BuildingCard());
		game.getBoardgame().getTowers()[2].getFloor()[0].setCard(game.getBuildingCards().get(0));
		assertTrue(game.getBuildingCards().get(0).getName().equals("Cappella"));
		CardMove move = new CardMove(test, test.getMembers().get(3), new Resource(0, 0, 1, 0, 0, 0, 0), 2, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
		}
		assertEquals(6, test.getPersonalBoard().getBuildingsCards().size());
		assertFalse(test.getMembers().get(3).isUsed());
		assertFalse(game.getBoardgame().getTowers()[2].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[2].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(2, test.getPersonalBoard().getResources().getWood().intValue());
		assertEquals(0, test.getPersonalBoard().getResources().getFaith().intValue());
	}

	
	@Test
	public void testFirstVentureMove() throws IOException {
		f.getDevCards(game);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		game.getPlayersOrder().get(0).getPersonalBoard().getResources().setMilitary(4);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().isEmpty());
		game.getBoardgame().getTowers()[3].getFloor()[0].setCard(game.getVentureCards().get(0));
		assertTrue(game.getVentureCards().get(0).getName().equals("Ingaggiatore Reclute"));
		CardMove move = new CardMove(game.getPlayersOrder().get(0), game.getPlayersOrder().get(0).getMembers().get(1),
				new Resource(0, 0, 0, 0, 0, 0, 0), 3, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
			e.printStackTrace();
		}		
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().size() == 1);
		assertTrue(game.getPlayersOrder().get(0).getMembers().get(1).isUsed());
		assertTrue(game.getBoardgame().getTowers()[3].isOccupied());
		assertNotNull(game.getBoardgame().getTowers()[3].getFloor()[0].getSpace().getMember());
		assertTrue(game.getBoardgame().getTowers()[3].getColoredMembersOnIt().contains(game.getPlayersOrder().get(0).getColor()));
		assertEquals(9, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary().intValue());
		assertEquals(1, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins().intValue());
	}
	
	@Test
	public void testMoreVentureMove() throws IOException {
		f.getDevCards(game);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().isEmpty());
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		game.getPlayersOrder().get(0).getPersonalBoard().getResources().setMilitary(4);
		game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().add(new VentureCard());
		game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().add(new VentureCard());
		game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().add(new VentureCard());
		game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().add(new VentureCard());
		game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().add(new VentureCard());
		game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().add(new VentureCard());
		assertFalse(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().isEmpty());
		game.getBoardgame().getTowers()[3].getFloor()[0].setCard(game.getVentureCards().get(0));
		assertTrue(game.getVentureCards().get(0).getName().equals("Ingaggiatore Reclute"));
		CardMove move = new CardMove(game.getPlayersOrder().get(0), game.getPlayersOrder().get(0).getMembers().get(1),
				new Resource(0, 0, 0, 0, 0, 0, 0), 3, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
		}		
		assertEquals(6, game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().size());
		assertFalse(game.getPlayersOrder().get(0).getMembers().get(1).isUsed());
		assertFalse(game.getBoardgame().getTowers()[3].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[3].getColoredMembersOnIt().contains(game.getPlayersOrder().get(0).getColor()));
		assertEquals(4, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary().intValue());
		assertEquals(5, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins().intValue());
	}
	
	@Test
	public void testOccupiedSpaceMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		game.getBoardgame().getTowers()[0].getFloor()[0].getSpace().setMember(new FamilyMember(test, "Colored"));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(game.getBoardgame().getTowers()[0].isOccupied());
		assertEquals(5, test.getPersonalBoard().getResources().getCoins().intValue());
	}
	
	@Test
	public void testNoPowerMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		game.getBoardgame().getTowers()[0].getFloor()[0].getSpace().setMember(new FamilyMember(test, "Colored"));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(3), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(game.getBoardgame().getTowers()[0].isOccupied());
		assertEquals(5, test.getPersonalBoard().getResources().getCoins().intValue());
	}
	
	@Test
	public void testOccupiedTowerMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		test.getEffects().add(new DoubleServantsEx());
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].setOccupied(true);
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().size() == 1);
		assertTrue(test.getMembers().get(0).isUsed());
		assertNotNull(game.getBoardgame().getTowers()[0].getFloor()[0].getSpace().getMember());
		assertTrue(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
	}
	
	@Test
	public void testSameColorTowerMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		test.getEffects().add(new DoubleServantsEx());
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].setOccupied(true);
		game.getBoardgame().getTowers()[0].getColoredMembersOnIt().add(test.getColor());
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		assertFalse(test.getMembers().get(0).isUsed());
		assertTrue(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
	}
	
	/*
	 * DA TESTARE ANCORA (INVALID MOVE per le carte sviluppo)
	 * - iesima carta territory senza Military sufficienti
	 * - territory card con la carta che azzera la richiesta dei punti militari
	 * - mossa con santa rita
	 * - mossa dove non posso prendere una carta perchè costa troppo (1 per torre)
	 * - mossa con costo della carta scontato (due effetti diversi)
	 */
	
	@Test
	public void testMarketMove(){
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		MarketMove move = new MarketMove(test, test.getMembers().get(0), new Resource(0,0,0,0,0,0,0), 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 10);
		assertNotNull(game.getBoardgame().getMarket()[0]);
		assertTrue(game.getBoardgame().getMarket()[0].getMember().equals(test.getMembers().get(0)));
	}
	
	@Test
	public void testUsedMarketMove(){
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		game.getBoardgame().getMarket()[0].setMember(new FamilyMember(test, "Uncolored"));
		MarketMove move = new MarketMove(test, test.getMembers().get(0), new Resource(0,0,0,0,0,0,0), 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
		}
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertNotNull(game.getBoardgame().getMarket()[0]);
	}
	
	@Test
	public void testInOccupiedMarketMove(){
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		test.getEffects().add(new InOccupiedSpaceEffect());
		game.getBoardgame().getMarket()[0].setMember(new FamilyMember(test, "Uncolored"));
		MarketMove move = new MarketMove(test, test.getMembers().get(0), new Resource(0,0,0,0,0,0,0), 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.SEVERE, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 10);
		assertNotNull(game.getBoardgame().getMarket()[0]);
	}
	
	/*
	 * MANCA TUTTA LA PARTE DI WORK
	 */
	
	
}