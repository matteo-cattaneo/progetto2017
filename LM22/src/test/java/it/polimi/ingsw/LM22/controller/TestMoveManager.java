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
import it.polimi.ingsw.LM22.model.leader.DoubleResourceEffect;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.model.leader.NoMilitaryRequestEffect;
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
		mainGC = new MainGameController(pinfolist);
		game = mainGC.getGame();
		prova = new MoveManager(game, mainGC);
		effectManager = new EffectManager(prova);
		init = new InitialConfigurator(pinfolist, effectManager, mainGC);
		f = new FileParser();
	}

	/**
	 * First Good Territory Move (all checks gave true)
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().size() == 1);
		assertTrue(test.getMembers().get(0).isUsed());
		assertTrue(game.getBoardgame().getTowers()[0].isOccupied());
		assertNotNull(game.getBoardgame().getTowers()[0].getFloor()[0].getSpace().getMember());
		assertTrue(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 8);
	}

	/**
	 * Test to find if you can't take the 7th Territory card
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Territory Max Reached!");
		}
		assertEquals(6, test.getPersonalBoard().getTerritoriesCards().size());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(game.getBoardgame().getTowers()[0].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(5, test.getPersonalBoard().getResources().getCoins().intValue());
	}

	/**
	 * First Good Character Move (all checks gave true)
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Invalid Move!");
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

	/**
	 * Test to find if you can't take the 7th Character card
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Character Max Reached!");
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

	/**
	 * First Good Building Move (all checks gave true)
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Invalid Move!");
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

	/**
	 * Test to find if you can't take the 7th Building card
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Building Max Reached");
		}
		assertEquals(6, test.getPersonalBoard().getBuildingsCards().size());
		assertFalse(test.getMembers().get(3).isUsed());
		assertFalse(game.getBoardgame().getTowers()[2].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[2].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(2, test.getPersonalBoard().getResources().getWood().intValue());
		assertEquals(0, test.getPersonalBoard().getResources().getFaith().intValue());
	}

	/**
	 * First Good Venture Move (all checks gave true) with normal cost
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().size() == 1);
		assertTrue(game.getPlayersOrder().get(0).getMembers().get(1).isUsed());
		assertTrue(game.getBoardgame().getTowers()[3].isOccupied());
		assertNotNull(game.getBoardgame().getTowers()[3].getFloor()[0].getSpace().getMember());
		assertTrue(game.getBoardgame().getTowers()[3].getColoredMembersOnIt()
				.contains(game.getPlayersOrder().get(0).getColor()));
		assertEquals(9, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary().intValue());
		assertEquals(1, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins().intValue());
	}

	/**
	 * First Good Territory Move (all checks gave true) with the military cost
	 * 
	 * @throws IOException
	 */
	@Test
	public void testFirstVentureWithMilitaryCostMove() throws IOException {
		f.getDevCards(game);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		game.getPlayersOrder().get(0).getPersonalBoard().getResources().setMilitary(5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().isEmpty());
		game.getBoardgame().getTowers()[3].getFloor()[0].setCard(game.getVentureCards().get(1));
		assertTrue(game.getVentureCards().get(1).getName().equals("Combattere le Eresie"));
		CardMove move = new CardMove(game.getPlayersOrder().get(0), game.getPlayersOrder().get(0).getMembers().get(1),
				new Resource(0, 0, 0, 0, 0, 0, 0), 3, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().size() == 1);
		assertTrue(game.getPlayersOrder().get(0).getMembers().get(1).isUsed());
		assertTrue(game.getBoardgame().getTowers()[3].isOccupied());
		assertNotNull(game.getBoardgame().getTowers()[3].getFloor()[0].getSpace().getMember());
		assertTrue(game.getBoardgame().getTowers()[3].getColoredMembersOnIt()
				.contains(game.getPlayersOrder().get(0).getColor()));
		assertEquals(2, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary().intValue());
		assertEquals(2, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith().intValue());
	}

	/**
	 * Test to find if you can't take the 7th Venture card
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Venture Max Reached");
		}
		assertEquals(6, game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().size());
		assertFalse(game.getPlayersOrder().get(0).getMembers().get(1).isUsed());
		assertFalse(game.getBoardgame().getTowers()[3].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[3].getColoredMembersOnIt()
				.contains(game.getPlayersOrder().get(0).getColor()));
		assertEquals(4, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary().intValue());
		assertEquals(5, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins().intValue());
	}

	/**
	 * Test if a move fails due to already occupied card space
	 * 
	 * @throws IOException
	 */
	@Test
	public void testOccupiedSpaceMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		game.getBoardgame().getTowers()[0].getFloor()[0].getSpace().setMember(new FamilyMember(test, "Uncolored"));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move due to already occupied card space");
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(game.getBoardgame().getTowers()[0].isOccupied());
		assertEquals(5, test.getPersonalBoard().getResources().getCoins().intValue());
	}

	/**
	 * Test if a move fails due to not enough power of the member + servants
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Invalid Move due to not enough family member power");
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(game.getBoardgame().getTowers()[0].isOccupied());
		assertEquals(5, test.getPersonalBoard().getResources().getCoins().intValue());
	}

	/**
	 * Test if a move fails due to occupation of the tower that increase the
	 * cost and the player is not able to pay for
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().size() == 1);
		assertTrue(test.getMembers().get(0).isUsed());
		assertNotNull(game.getBoardgame().getTowers()[0].getFloor()[0].getSpace().getMember());
		assertTrue(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
	}

	/**
	 * Test if a move fails due to occupation of the tower of another family
	 * member of the same player (both not Uncolored)
	 * 
	 * @throws IOException
	 */
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
			LOGGER.log(Level.INFO, "Invalid Move due to family member of the same player already on the tower");
		}
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		assertFalse(test.getMembers().get(0).isUsed());
		assertTrue(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
	}

	/**
	 * Test if a move for a territory card doesn't fail due to Military Points
	 * leak of the player beacuse he has the Cesare Borgia activated (Leader
	 * Card that adds NoMilitaryRequestEffect)
	 * 
	 * @throws IOException
	 */
	@Test
	public void testNoMilitaryRequestMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		test.getEffects().add(new NoMilitaryRequestEffect());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertFalse(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertEquals(5, test.getPersonalBoard().getTerritoriesCards().size());
		assertTrue(test.getMembers().get(0).isUsed());
		assertTrue(test.getPersonalBoard().getTerritoriesCards().contains(game.getTerritoryCards().get(2)));
		assertTrue(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(8, test.getPersonalBoard().getResources().getCoins().intValue());
	}

	/**
	 * Test if a move for a territory card fails due to Military Points leak of
	 * the player
	 * 
	 * @throws IOException
	 */
	@Test
	public void testNoMilitaryEnoughMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		test.getPersonalBoard().getTerritoriesCards().add(new TerritoryCard());
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertFalse(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move due to Military Request");
		}
		assertEquals(3, test.getPersonalBoard().getTerritoriesCards().size());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(test.getPersonalBoard().getTerritoriesCards().contains(game.getTerritoryCards().get(2)));
		assertFalse(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(5, test.getPersonalBoard().getResources().getCoins().intValue());
	}

	/**
	 * Test if a move for a card with ResourcePrivilegeEffect as Immediate
	 * EFfect gives to the player, who has Santa Rita activated, double
	 * Resources only for (wood, stone, coins, servants)
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSantaRitaResourcePrivilegeEffectMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		test.getEffects().add(new DoubleResourceEffect());
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(18));
		assertTrue(game.getTerritoryCards().get(18).getName().equals("Castello"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertEquals(1, test.getPersonalBoard().getTerritoriesCards().size());
		assertTrue(test.getMembers().get(0).isUsed());
		assertTrue(test.getPersonalBoard().getTerritoriesCards().contains(game.getTerritoryCards().get(18)));
		assertTrue(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(9, test.getPersonalBoard().getResources().getCoins().intValue());
		assertEquals(2, test.getPersonalBoard().getResources().getVictory().intValue());
	}

	/**
	 * Test if a move for a territory card fails due to card cost
	 * 
	 * @throws IOException
	 */
	@Test
	public void testTerritoryTooHighCostMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		test.getPersonalBoard().getResources().setCoins(2);
		assertTrue(test.getPersonalBoard().getTerritoriesCards().isEmpty());
		game.getBoardgame().getTowers()[0].setOccupied(true);
		game.getBoardgame().getTowers()[0].getFloor()[0].setCard(game.getTerritoryCards().get(2));
		assertTrue(game.getTerritoryCards().get(2).getName().equals("Città"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Territory card too high cost");
		}
		assertEquals(0, test.getPersonalBoard().getTerritoriesCards().size());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(test.getPersonalBoard().getTerritoriesCards().contains(game.getTerritoryCards().get(2)));
		assertFalse(game.getBoardgame().getTowers()[0].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(2, test.getPersonalBoard().getResources().getCoins().intValue());
	}

	/**
	 * Test if a move for a character card fails due to card cost
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCharacterTooHighCostMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getCharactersCards().isEmpty());
		test.getPersonalBoard().getResources().setCoins(1);
		game.getBoardgame().getTowers()[1].getFloor()[0].setCard(game.getCharacterCards().get(0));
		assertTrue(game.getCharacterCards().get(0).getName().equals("Predicatore"));
		CardMove move = new CardMove(test, test.getMembers().get(0), new Resource(0, 0, 2, 0, 0, 0, 0), 1, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
		}
		assertTrue(test.getPersonalBoard().getCharactersCards().isEmpty());
		assertFalse(test.getMembers().get(0).isUsed());
		assertFalse(game.getBoardgame().getTowers()[1].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[1].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(3, test.getPersonalBoard().getResources().getServants().intValue());
		assertEquals(1, test.getPersonalBoard().getResources().getCoins().intValue());
		assertEquals(0, test.getPersonalBoard().getResources().getFaith().intValue());
		assertFalse(test.getEffects().contains(game.getCharacterCards().get(0).getPermanentEffect()));
	}

	/**
	 * Test if a move for a building card fails due to card cost
	 * 
	 * @throws IOException
	 */
	@Test
	public void testBuildingTooHighCostMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getBuildingsCards().isEmpty());
		test.getPersonalBoard().getResources().setWood(1);
		game.getBoardgame().getTowers()[2].getFloor()[0].setCard(game.getBuildingCards().get(0));
		assertTrue(game.getBuildingCards().get(0).getName().equals("Cappella"));
		CardMove move = new CardMove(test, test.getMembers().get(3), new Resource(0, 0, 1, 0, 0, 0, 0), 2, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Building Card too high cost");
		}
		assertTrue(test.getPersonalBoard().getBuildingsCards().isEmpty());
		assertFalse(test.getMembers().get(3).isUsed());
		assertFalse(game.getBoardgame().getTowers()[2].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[2].getColoredMembersOnIt().contains(test.getColor()));
		assertEquals(1, test.getPersonalBoard().getResources().getWood().intValue());
		assertEquals(0, test.getPersonalBoard().getResources().getFaith().intValue());
	}

	/**
	 * Test if a move for a venture card fails due to card military cost
	 * 
	 * @throws IOException
	 */
	@Test
	public void testVentureTooHighMilitaryCostMove() throws IOException {
		f.getDevCards(game);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().isEmpty());
		game.getBoardgame().getTowers()[3].getFloor()[0].setCard(game.getVentureCards().get(1));
		assertTrue(game.getVentureCards().get(1).getName().equals("Combattere le Eresie"));
		CardMove move = new CardMove(game.getPlayersOrder().get(0), game.getPlayersOrder().get(0).getMembers().get(1),
				new Resource(0, 0, 0, 0, 0, 0, 0), 3, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Venture card too high military cost");
		}
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().isEmpty());
		assertFalse(game.getPlayersOrder().get(0).getMembers().get(1).isUsed());
		assertFalse(game.getBoardgame().getTowers()[3].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[3].getColoredMembersOnIt()
				.contains(game.getPlayersOrder().get(0).getColor()));
		assertEquals(0, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary().intValue());
		assertEquals(5, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins().intValue());
	}

	/**
	 * Test if a move for a venture card fails due to card cost
	 * 
	 * @throws IOException
	 */
	@Test
	public void testVentureTooHighCostMove() throws IOException {
		f.getDevCards(game);
		assertEquals(5, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins().intValue());
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().isEmpty());
		game.getPlayersOrder().get(0).getPersonalBoard().getResources().setCoins(3);
		assertEquals(3, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins().intValue());
		game.getBoardgame().getTowers()[3].getFloor()[0].setCard(game.getVentureCards().get(0));
		assertTrue(game.getVentureCards().get(0).getName().equals("Ingaggiatore Reclute"));
		CardMove move = new CardMove(game.getPlayersOrder().get(0), game.getPlayersOrder().get(0).getMembers().get(1),
				new Resource(0, 0, 0, 0, 0, 0, 0), 3, 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Venture card too high cost");
		}
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getVenturesCards().isEmpty());
		assertFalse(game.getPlayersOrder().get(0).getMembers().get(1).isUsed());
		assertFalse(game.getBoardgame().getTowers()[3].isOccupied());
		assertFalse(game.getBoardgame().getTowers()[3].getColoredMembersOnIt()
				.contains(game.getPlayersOrder().get(0).getColor()));
		assertEquals(0, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary().intValue());
		assertEquals(3, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins().intValue());
	}

	/*
	 * - mossa con costo della carta scontato (due effetti diversi)
	 */

	/** END CARD MOVE TESTS */

	/** MARKET MOVE TESTS */
	@Test
	public void testMarketMove() {
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		MarketMove move = new MarketMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 10);
		assertNotNull(game.getBoardgame().getMarket()[0]);
		assertTrue(game.getBoardgame().getMarket()[0].getMember().equals(test.getMembers().get(0)));
	}

	@Test
	public void testUsedMarketMove() {
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		game.getBoardgame().getMarket()[0].setMember(new FamilyMember(test, "Uncolored"));
		MarketMove move = new MarketMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move due to already used market space");
		}
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertNotNull(game.getBoardgame().getMarket()[0]);
	}

	@Test
	public void testInOccupiedMarketMove() {
		Player test = game.getPlayersOrder().get(0);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		test.getEffects().add(new InOccupiedSpaceEffect());
		game.getBoardgame().getMarket()[0].setMember(new FamilyMember(test, "Uncolored"));
		MarketMove move = new MarketMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), 0);
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 10);
		assertNotNull(game.getBoardgame().getMarket()[0]);
	}

	/** END MARKET MOVE TESTS */

	@Test
	public void testHarvestMove() throws IOException {
		// 4 giocatori
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(0);
		test.getPersonalBoard().setBonusBoard(game.getPersonalBonusTile()[0]);
		assertTrue(test.getPersonalBoard().getResources().getWood() == 2);
		assertTrue(test.getPersonalBoard().getResources().getStone() == 2);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(test.getPersonalBoard().getResources().getMilitary() == 0);
		test.getPersonalBoard().getTerritoriesCards().add(game.getTerritoryCards().get(0));
		test.getMembers().get(0).setValue(5);
		WorkMove move = new WorkMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), "HARVEST");
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getResources().getWood() == 3);
		assertTrue(test.getPersonalBoard().getResources().getStone() == 4);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 6);
		assertTrue(test.getPersonalBoard().getResources().getMilitary() == 2);

		// due giocatori
		game.getPlayersOrder().remove(3);
		game.getPlayersOrder().remove(2);
		game.getBoardgame().getHarvestSpace().setColoredMemberOnIt(new ArrayList<String>());
		game.getBoardgame().getHarvestSpace().setMembers(new ArrayList<FamilyMember>());
		test.getMembers().get(1).setValue(5);
		WorkMove move2 = new WorkMove(test, test.getMembers().get(1), new Resource(0, 0, 0, 0, 0, 0, 0), "HARVEST");
		try {
			prova.manageMove(move2);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertEquals(4, test.getPersonalBoard().getResources().getWood().intValue());
		assertTrue(test.getPersonalBoard().getResources().getStone() == 6);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 7);
		assertTrue(test.getPersonalBoard().getResources().getMilitary() == 4);
	}

	@Test
	public void testProductionMove() throws IOException {
		f.getDevCards(game);
		Player test = game.getPlayersOrder().get(1);
		test.getPersonalBoard().getBuildingsCards().add(game.getBuildingCards().get(23));
		test.getPersonalBoard().setBonusBoard(game.getPersonalBonusTile()[0]);
		assertTrue(test.getPersonalBoard().getResources().getServants() == 3);
		assertTrue(test.getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(test.getPersonalBoard().getResources().getVictory() == 0);
		WorkMove move = new WorkMove(test, test.getMembers().get(0), new Resource(0, 0, 0, 0, 0, 0, 0), "PRODUCTION");
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getResources().getServants() == 4);
		assertTrue(test.getPersonalBoard().getResources().getMilitary() == 2);
		assertTrue(test.getPersonalBoard().getResources().getVictory() == 3);

		// 2 giocatori
		game.getPlayersOrder().remove(3);
		game.getPlayersOrder().remove(2);
		game.getBoardgame().getProductionSpace().setColoredMemberOnIt(new ArrayList<String>());
		game.getBoardgame().getProductionSpace().setMembers(new ArrayList<FamilyMember>());
		WorkMove move2 = new WorkMove(test, test.getMembers().get(1), new Resource(0, 0, 0, 0, 0, 0, 0), "PRODUCTION");
		try {
			prova.manageMove(move2);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getResources().getServants() == 5);
		assertTrue(test.getPersonalBoard().getResources().getMilitary() == 4);
		assertTrue(test.getPersonalBoard().getResources().getVictory() == 6);
	}

	@Test
	public void testLeaderActivation() throws IOException {
		f.getLeaderCards(game);
		Player test = game.getPlayersOrder().get(0);
		test.getLeaderCards().add(game.getLeaderCards().get(4));
		test.getPersonalBoard().getResources().setCoins(18);
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 18);
		assertTrue(test.getPersonalBoard().getResources().getFaith() == 0);
		LeaderCardActivation move = new LeaderCardActivation(test, game.getLeaderCards().get(4));
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
		assertTrue(test.getPersonalBoard().getResources().getCoins() == 18);
		assertTrue(test.getPersonalBoard().getResources().getFaith() == 1);
	}

	@Test
	public void testEndMove() throws IOException {
		Player test = game.getPlayersOrder().get(0);
		EndMove move = new EndMove(test, "noError");
		try {
			prova.manageMove(move);
		} catch (InvalidMoveException e) {
			LOGGER.log(Level.INFO, "Invalid Move!");
			e.printStackTrace();
		}
	}
}