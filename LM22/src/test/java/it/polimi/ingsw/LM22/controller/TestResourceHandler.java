package it.polimi.ingsw.LM22.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;
import junit.framework.TestCase;

public class TestResourceHandler extends TestCase {
	ResourceHandler rh = new ResourceHandler();
	Resource firstResource, secondResource, nothing, result, temp;
	Integer molt;
	Resource wood, stone, servant, coin, faith, military, victory;

	// assigning the values
	public void setUp() {
		firstResource = new Resource(0, 2, 0, 5, 9, 2, 3);
		secondResource = new Resource(1, 2, 0, 4, 0, 1, 4);
		nothing = new Resource(0, 0, 0, 0, 0, 0, 0);
		molt = 2;
		wood = new Resource(1, 0, 0, 0, 0, 0, 0);
		stone = new Resource(0, 1, 0, 0, 0, 0, 0);
		servant = new Resource(0, 0, 1, 0, 0, 0, 0);
		coin = new Resource(0, 0, 0, 1, 0, 0, 0);
		faith = new Resource(0, 0, 0, 0, 1, 0, 0);
		military = new Resource(0, 0, 0, 0, 0, 1, 0);
		victory = new Resource(0, 0, 0, 0, 0, 0, 1);
	}

	@Test
	public void testCardDiscounted() {
		result = new Resource(1, 1, 1, 1, 1, 1, 1);

		temp = rh.cardDiscounted(result, stone);
		assertEquals(1, temp.getWood().intValue());
		assertEquals(0, temp.getStone().intValue());
		assertEquals(1, temp.getServants().intValue());
		assertEquals(1, temp.getCoins().intValue());
		assertEquals(1, temp.getFaith().intValue());
		assertEquals(1, temp.getMilitary().intValue());
		assertEquals(1, temp.getVictory().intValue());

		temp = rh.cardDiscounted(result, wood);
		assertEquals(0, temp.getWood().intValue());
		assertEquals(1, temp.getStone().intValue());
		assertEquals(1, temp.getServants().intValue());
		assertEquals(1, temp.getCoins().intValue());
		assertEquals(1, temp.getFaith().intValue());
		assertEquals(1, temp.getMilitary().intValue());
		assertEquals(1, temp.getVictory().intValue());

		temp = rh.cardDiscounted(result, servant);
		assertEquals(1, temp.getWood().intValue());
		assertEquals(1, temp.getStone().intValue());
		assertEquals(0, temp.getServants().intValue());
		assertEquals(1, temp.getCoins().intValue());
		assertEquals(1, temp.getFaith().intValue());
		assertEquals(1, temp.getMilitary().intValue());
		assertEquals(1, temp.getVictory().intValue());

		temp = rh.cardDiscounted(result, coin);
		assertEquals(1, temp.getWood().intValue());
		assertEquals(1, temp.getStone().intValue());
		assertEquals(1, temp.getServants().intValue());
		assertEquals(0, temp.getCoins().intValue());
		assertEquals(1, temp.getFaith().intValue());
		assertEquals(1, temp.getMilitary().intValue());
		assertEquals(1, temp.getVictory().intValue());

		temp = rh.cardDiscounted(result, faith);
		assertEquals(1, temp.getWood().intValue());
		assertEquals(1, temp.getStone().intValue());
		assertEquals(1, temp.getServants().intValue());
		assertEquals(1, temp.getCoins().intValue());
		assertEquals(0, temp.getFaith().intValue());
		assertEquals(1, temp.getMilitary().intValue());
		assertEquals(1, temp.getVictory().intValue());

		temp = rh.cardDiscounted(result, military);
		assertEquals(1, temp.getWood().intValue());
		assertEquals(1, temp.getStone().intValue());
		assertEquals(1, temp.getServants().intValue());
		assertEquals(1, temp.getCoins().intValue());
		assertEquals(1, temp.getFaith().intValue());
		assertEquals(0, temp.getMilitary().intValue());
		assertEquals(1, temp.getVictory().intValue());

		temp = rh.cardDiscounted(result, victory);
		assertEquals(1, temp.getWood().intValue());
		assertEquals(1, temp.getStone().intValue());
		assertEquals(1, temp.getServants().intValue());
		assertEquals(1, temp.getCoins().intValue());
		assertEquals(1, temp.getFaith().intValue());
		assertEquals(1, temp.getMilitary().intValue());
		assertEquals(0, temp.getVictory().intValue());
	}

	@Test
	public void testAddResource() {
		result = new Resource(1, 2, 3, 4, 5, 6, 7);
		rh.addResource(result, stone);
		assertEquals(1, result.getWood().intValue());
		assertEquals(3, result.getStone().intValue());
		assertEquals(3, result.getServants().intValue());
		assertEquals(4, result.getCoins().intValue());
		assertEquals(5, result.getFaith().intValue());
		assertEquals(6, result.getMilitary().intValue());
		assertEquals(7, result.getVictory().intValue());
	}

	@Test
	public void testSubResource() {
		result = new Resource(1, 2, 3, 4, 5, 6, 7);
		rh.subResource(result, coin);
		assertEquals(1, result.getWood().intValue());
		assertEquals(2, result.getStone().intValue());
		assertEquals(3, result.getServants().intValue());
		assertEquals(3, result.getCoins().intValue());
		assertEquals(5, result.getFaith().intValue());
		assertEquals(6, result.getMilitary().intValue());
		assertEquals(7, result.getVictory().intValue());
	}

	@Test
	public void testEnoughResources() {
		assertEquals(true, rh.enoughResources(firstResource, nothing));

		assertEquals(false, rh.enoughResources(nothing, wood));
		assertEquals(false, rh.enoughResources(nothing, stone));
		assertEquals(false, rh.enoughResources(nothing, servant));
		assertEquals(false, rh.enoughResources(nothing, coin));
		assertEquals(false, rh.enoughResources(nothing, faith));
		assertEquals(false, rh.enoughResources(nothing, military));
		assertEquals(false, rh.enoughResources(nothing, victory));
	}

	@Test
	public void testEnoughResources2() throws RemoteException {
		ArrayList<PlayerInfo> pinfolist = new ArrayList<PlayerInfo>();
		PlayerInfo pi1 = new PlayerInfo();
		PlayerInfo pi2 = new PlayerInfo();
		pi1.setName("Nicola");
		pi2.setName("Matteo");
		pinfolist.add(pi1);
		pinfolist.add(pi2);
		MainGameController mainGC = new MainGameController(pinfolist);
		Game game = mainGC.getGame();
		MoveManager moveManager = new MoveManager(game, mainGC);
		EffectManager effectManager = new EffectManager(moveManager);
		InitialConfigurator init = new InitialConfigurator(pinfolist, rh, effectManager, mainGC);
		init.initializeTurn(game);
		Resource additionalCost = new Resource(0, 0, 0, 3, 0, 0, 0);
		CardMove move = new CardMove(game.getPlayersOrder().get(0), game.getPlayersOrder().get(0).getMembers().get(0),
				new Resource(0, 0, 0, 0, 0, 0, 0), 0, 0);
		assertTrue(rh.enoughResources(move, additionalCost));
		Resource additionalCost2 = new Resource(0, 0, 0, 0, 1, 0, 0);
		CardMove move2 = new CardMove(game.getPlayersOrder().get(0), game.getPlayersOrder().get(0).getMembers().get(3),
				new Resource(0, 0, 0, 0, 0, 0, 0), 0, 1);
		assertFalse(rh.enoughResources(move2, additionalCost2));
	}

	@Test
	public void testDiffResource() {
		result = rh.diffResource(firstResource, secondResource);
		assertEquals(-1, result.getWood().intValue());
		assertEquals(0, result.getStone().intValue());
		assertEquals(0, result.getServants().intValue());
		assertEquals(1, result.getCoins().intValue());
		assertEquals(9, result.getFaith().intValue());
		assertEquals(1, result.getMilitary().intValue());
		assertEquals(-1, result.getVictory().intValue());
	}

	@Test
	public void testMoltiplicatorResource() {
		result = rh.resourceMultiplication(firstResource, molt);
		assertEquals(0, result.getWood().intValue());
		assertEquals(4, result.getStone().intValue());
		assertEquals(0, result.getServants().intValue());
		assertEquals(10, result.getCoins().intValue());
		assertEquals(18, result.getFaith().intValue());
		assertEquals(4, result.getMilitary().intValue());
		assertEquals(6, result.getVictory().intValue());
	}

	@Test
	public void testSumResource() {
		result = rh.sumResource(firstResource, secondResource);
		assertEquals(1, result.getWood().intValue());
		assertEquals(4, result.getStone().intValue());
		assertEquals(0, result.getServants().intValue());
		assertEquals(9, result.getCoins().intValue());
		assertEquals(9, result.getFaith().intValue());
		assertEquals(3, result.getMilitary().intValue());
		assertEquals(7, result.getVictory().intValue());
	}

	@Test
	public void testEqualResource() {
		assertEquals(false, rh.equalResources(wood, nothing));
		assertEquals(false, rh.equalResources(wood, nothing));
		assertEquals(false, rh.equalResources(stone, nothing));
		assertEquals(false, rh.equalResources(servant, nothing));
		assertEquals(false, rh.equalResources(coin, nothing));
		assertEquals(false, rh.equalResources(faith, nothing));
		assertEquals(false, rh.equalResources(military, nothing));
		assertEquals(false, rh.equalResources(victory, nothing));

		assertEquals(true, rh.equalResources(nothing, nothing));
	}
}
