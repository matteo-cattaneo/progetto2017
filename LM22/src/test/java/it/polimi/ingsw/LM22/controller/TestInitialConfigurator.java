package it.polimi.ingsw.LM22.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.FamilyMember;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;
import junit.framework.TestCase;

public class TestInitialConfigurator extends TestCase {

	InitialConfigurator prova;
	Game game;
	final String[] colors = { "Orange", "Black", "White" };

	public void setUp() throws RemoteException {
		ArrayList<PlayerInfo> pinfolist = new ArrayList<PlayerInfo>();
		PlayerInfo pi1 = new PlayerInfo();
		PlayerInfo pi2 = new PlayerInfo();
		PlayerInfo pi3 = new PlayerInfo();
		pinfolist.add(pi1);
		pinfolist.add(pi2);
		pinfolist.add(pi3);
		game = new Game();
		Player p1 = new Player("Nicola", "Blue");
		Player p2 = new Player("Matteo", "Green");
		Player p3 = new Player("Esempio", "Red");
		game.setPlayersOrder(new ArrayList<Player>());
		game.getPlayersOrder().add(p1);
		game.getPlayersOrder().add(p2);
		game.getPlayersOrder().add(p3);
		ResourceHandler r = new ResourceHandler();
		MainGameController mainGC = new MainGameController(pinfolist);
		MoveManager moveManager = new MoveManager(game, mainGC);
		EffectManager effectManager = new EffectManager(moveManager);
		prova = new InitialConfigurator(pinfolist, r, effectManager, mainGC);
	}

	@Test
	public void testInitializeTurn() {
		prova.initializeTurn(game);
		/**
		 * setUp period & round
		 */
		assertEquals(1, game.getPeriod().intValue());
		assertEquals(1, game.getPeriod().intValue());
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
		 * giveInitialResources test
		 */
		int i = 0;
		for (Player p: game.getPlayersOrder()){				
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
			for (int k = 0; k < 4; k++){
				assertTrue(game.getBoardgame().getTowers()[j].getFloor()[k].getSpace() != null
						&& game.getBoardgame().getTowers()[j].getFloor()[k].getCard() != null
						&& game.getBoardgame().getTowers()[j].getFloor()[k].getCard().getPeriod() == 1);
			}
		/*for (Player p: game.getPlayersOrder())
			assertTrue(p.getActivatedLeaderCards().isEmpty() && p.getHandLeaderCards().size() == 4 && p.getLeaderCards().isEmpty());
			*/
	}
	
}
