package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;
import junit.framework.TestCase;

public class TestVaticanReportManager extends TestCase{

	InitialConfigurator init;
	Game game;
	PlayerInfo pi1;
	PlayerInfo pi2;
	PlayerInfo pi3;
	PlayerInfo pi4;
	ResourceHandler r;
	MainGameController mainGC;
	MoveManager moveManager;
	EffectManager effectManager;
	VaticanReportManager prova;

	public void setUp() throws RemoteException {
		ArrayList<PlayerInfo> pinfolist = new ArrayList<PlayerInfo>();
		pi1 = new PlayerInfo();
		pi2 = new PlayerInfo();
		pi3 = new PlayerInfo();
		pi4 = new PlayerInfo();
		pi1.setName("Nicola");
		pi2.setName("Matteo");
		pi3.setName("Esempio");
		pi4.setName("Esempio1");
		pinfolist.add(pi1);
		pinfolist.add(pi2);
		pinfolist.add(pi3);
		pinfolist.add(pi4);
		game = new Game();
		r = new ResourceHandler();
		mainGC = new MainGameController(pinfolist);
		moveManager = new MoveManager(game, mainGC);
		prova = new VaticanReportManager();
		effectManager = new EffectManager(moveManager);
		init = new InitialConfigurator(pinfolist, r, effectManager, mainGC);
	}
	
	@Test
	public void testVaticanReport(){
		init.initializeTurn(game);
		for (Player p: game.getPlayersOrder()){
			assertTrue(p.getEffects().isEmpty());
		}
		try {
			prova.manageVaticanReport(game, mainGC);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Player p: game.getPlayersOrder()){
			assertTrue(!p.getEffects().isEmpty());
			assertTrue(p.getEffects().contains(game.getBoardgame().getFaithGrid().getExCommunication(1).getEffect()));
		}
	}
}