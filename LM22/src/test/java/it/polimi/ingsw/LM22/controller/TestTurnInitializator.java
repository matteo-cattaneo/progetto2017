package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;
import junit.framework.TestCase;

public class TestTurnInitializator extends TestCase {
	TurnInizializator turnInizializator;
	Game game;
	final String[] colors = { "Orange", "Black", "White" };

	public void setUp() throws RemoteException {
		ArrayList<PlayerInfo> pinfolist = new ArrayList<PlayerInfo>();
		PlayerInfo pi1 = new PlayerInfo();
		PlayerInfo pi2 = new PlayerInfo();
		PlayerInfo pi3 = new PlayerInfo();
		PlayerInfo pi4 = new PlayerInfo();
		pi1.setName("Nicola");
		pi2.setName("Matteo");
		pi3.setName("Esempio");
		pi4.setName("Esempio1");
		pinfolist.add(pi1);
		pinfolist.add(pi2);
		pinfolist.add(pi3);
		pinfolist.add(pi4);
		MainGameController mainGC = new MainGameController(pinfolist);
		game = mainGC.getGame();
		MoveManager moveManager = new MoveManager(game, mainGC);
		EffectManager effectManager = new EffectManager(moveManager);
		turnInizializator = new TurnInizializator(effectManager, mainGC);
	}

	@Test
	public void testInitializeTurn() throws IOException {
		assertEquals(1, game.getPeriod().intValue());
		assertEquals(1, game.getRound().intValue());
		/**
		 * giveInitialResources test
		 */
		int i = 0;
		for (Player p : game.getPlayersOrder()) {
			assertTrue(p.getPersonalBoard().getResources().getWood().intValue() == 2);
			assertTrue(p.getPersonalBoard().getResources().getStone().intValue() == 2);
			assertTrue(p.getPersonalBoard().getResources().getServants().intValue() == 3);
			assertTrue(p.getPersonalBoard().getResources().getMilitary().intValue() == 0);
			assertTrue(p.getPersonalBoard().getResources().getVictory().intValue() == 0);
			assertTrue(p.getPersonalBoard().getResources().getFaith().intValue() == 0);
			assertTrue(p.getPersonalBoard().getResources().getCoins().intValue() == 5 + i);
			i++;
		}
		/**
		 * distributions tests
		 */
		for (int j = 0; j < 4; j++)
			for (int k = 0; k < 4; k++) {
				assertTrue(game.getBoardgame().getTowers()[j].getFloor()[k].getSpace() != null
						&& game.getBoardgame().getTowers()[j].getFloor()[k].getCard() != null
						&& game.getBoardgame().getTowers()[j].getFloor()[k].getCard().getPeriod() == 1);
			}
		turnInizializator.initializeTurn(game);
		/**
		 * setUp period & round
		 */
		assertEquals(1, game.getPeriod().intValue());
		assertEquals(2, game.getRound().intValue());
		/**
		 * throwDices + setUpPlayers tests
		 */
		for (int cont = 0; cont < 3; cont++)
			assertTrue(game.getBoardgame().getDice(colors[cont]) > 0 && game.getBoardgame().getDice(colors[cont]) < 7);
		for (Player p : game.getPlayersOrder()) {
			for (FamilyMember fm : p.getMembers()) {
				if (fm.getColor() != "Uncolored")
					assertTrue(game.getBoardgame().getDice(fm.getColor()).intValue() == fm.getValue().intValue());
				else
					assertTrue(fm.getValue() == 0);
			}
			assertEquals(4, p.getMembers().size());
		}
		/**
		 * distributions tests
		 */
		for (int j = 0; j < 4; j++)
			for (int k = 0; k < 4; k++) {
				assertTrue(game.getBoardgame().getTowers()[j].getFloor()[k].getSpace() != null
						&& game.getBoardgame().getTowers()[j].getFloor()[k].getCard() != null
						&& game.getBoardgame().getTowers()[j].getFloor()[k].getCard().getPeriod() == 1);
			}
		/*
		 * for (Player p: game.getPlayersOrder())
		 * assertTrue(p.getActivatedLeaderCards().isEmpty() &&
		 * p.getHandLeaderCards().size() == 4 && p.getLeaderCards().isEmpty());
		 */
		turnInizializator.initializeTurn(game);
		assertEquals(2, game.getPeriod().intValue());
		assertEquals(1, game.getRound().intValue());
	}
}
