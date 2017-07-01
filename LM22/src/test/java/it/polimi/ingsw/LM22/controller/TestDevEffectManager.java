package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;
import junit.framework.TestCase;

public class TestDevEffectManager extends TestCase {

	InitialConfigurator init;
	TurnInizializator turnInizializator;
	Game game;
	EffectManager effectManager;
	MoveManager moveManager;
	MainGameController mainGC;
	FileParser f;
	ResourceHandler r;
	final String[] colors = { "Orange", "Black", "White" };

	public void setUp() throws IOException {
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
		r = new ResourceHandler();
		mainGC = new MainGameController(pinfolist);
		game = mainGC.getGame();
		moveManager = new MoveManager(game, mainGC);
		effectManager = new EffectManager(moveManager);
		init = new InitialConfigurator(pinfolist, r, effectManager, mainGC);
		turnInizializator = new TurnInizializator(effectManager, r, mainGC);
		f = new FileParser();
		f.getDevCards(game);
	}

	@Test
	public void testResourcePrivilegeEffect() {
		TerritoryCard città = game.getTerritoryCards().get(2);
		assertTrue(città.getName().equals("Città"));
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
		effectManager.manageEffect(città.getImmediateEffect(), game.getPlayersOrder().get(0),
				game.getPlayersOrder().get(0).getPersonalBoard().getResources(), mainGC);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 8);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
	}

	@Test
	public void testResourceToResourcePrivilegeEffect() {
		CharacterCard generale = game.getCharacterCards().get(17);
		assertTrue(generale.getName().equals("Generale"));
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
		game.getPlayersOrder().get(0).getPersonalBoard().getResources().setMilitary(10);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 10);
		effectManager.manageEffect(generale.getImmediateEffect(), game.getPlayersOrder().get(0),
				game.getPlayersOrder().get(0).getPersonalBoard().getResources(), mainGC);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 10);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 5);
	}
	
	@Test
	public void testChangeEffect() {
		BuildingCard cappella = game.getBuildingCards().get(0);
		assertTrue(cappella.getName().equals("Cappella"));
		game.getPlayersOrder().get(0).getPersonalBoard().getResources().setCoins(0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
		effectManager.manageEffect(cappella.getPermanentEffect(), game.getPlayersOrder().get(0),
				game.getPlayersOrder().get(0).getPersonalBoard().getResources(), mainGC);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
	}
	
	@Test
	public void testDoubleChangeEffect() {
		BuildingCard falegnameria = game.getBuildingCards().get(1);
		assertTrue(falegnameria.getName().equals("Falegnameria"));
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		game.getPlayersOrder().get(0).getPersonalBoard().getResources().setWood(0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
		effectManager.manageEffect(falegnameria.getPermanentEffect(), game.getPlayersOrder().get(0),
				game.getPlayersOrder().get(0).getPersonalBoard().getResources(), mainGC);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
	}
	
	@Test
	public void testCardToResourceCharEffect() {
		BuildingCard teatro = game.getBuildingCards().get(2);
		assertTrue(teatro.getName().equals("Teatro"));
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
		effectManager.manageEffect(teatro.getPermanentEffect(), game.getPlayersOrder().get(0),
				game.getPlayersOrder().get(0).getPersonalBoard().getResources(), mainGC);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
	}
	
	@Test
	public void testCardToResourceVentEffect() {
		BuildingCard arcoDiTrionfo = game.getBuildingCards().get(3);
		assertTrue(arcoDiTrionfo.getName().equals("Arco di Trionfo"));
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
		effectManager.manageEffect(arcoDiTrionfo.getPermanentEffect(), game.getPlayersOrder().get(0),
				game.getPlayersOrder().get(0).getPersonalBoard().getResources(), mainGC);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
	}
	
	@Test
	public void testCardToResourceBuilEffect() {
		BuildingCard zecca = game.getBuildingCards().get(4);
		assertTrue(zecca.getName().equals("Zecca"));
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
		effectManager.manageEffect(zecca.getPermanentEffect(), game.getPlayersOrder().get(0),
				game.getPlayersOrder().get(0).getPersonalBoard().getResources(), mainGC);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
	}
	
	@Test
	public void testCardToResourceTerrEffect() {
		BuildingCard esattoria = game.getBuildingCards().get(5);
		assertTrue(esattoria.getName().equals("Esattoria"));
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
		effectManager.manageEffect(esattoria.getPermanentEffect(), game.getPlayersOrder().get(0),
				game.getPlayersOrder().get(0).getPersonalBoard().getResources(), mainGC);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getCoins() == 5);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getServants() == 3);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getStone() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getWood() == 2);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getMilitary() == 0);
		assertTrue(game.getPlayersOrder().get(0).getPersonalBoard().getResources().getVictory() == 0);
	}
}
