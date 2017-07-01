package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.leader.ChurchSubstainEffect;
import it.polimi.ingsw.LM22.model.leader.CoinsDiscountEffect;
import it.polimi.ingsw.LM22.model.leader.DoubleResourceEffect;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import it.polimi.ingsw.LM22.model.leader.MemberBonusEffect;
import it.polimi.ingsw.LM22.model.leader.MemberChangeEffect;
import it.polimi.ingsw.LM22.model.leader.NoMilitaryRequestEffect;
import it.polimi.ingsw.LM22.model.leader.NoOccupiedTowerEffect;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;
import junit.framework.TestCase;

public class TestLeaderManageEffect extends TestCase {
	Game game;
	EffectManager effectManager;
	MainGameController mainGC;
	MoveManager moveManager;
	List<LeaderCard> leader;

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
		mainGC = new MainGameController(pinfolist);
		game = mainGC.getGame();
		moveManager = new MoveManager(game, mainGC);
		effectManager = new EffectManager(moveManager);
		FileParser fp = new FileParser();
		fp.getLeaderCards(game);
	}

	@Test
	public void testChurchSubstainEffect() throws IOException {
		assertFalse(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), ChurchSubstainEffect.class));
		effectManager.leaderEffectManage(game.getLeaderCards().get(10).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(10), mainGC);
		assertTrue(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), ChurchSubstainEffect.class));
	}

	@Test
	public void testCoinsDiscountEffect() throws IOException {
		assertFalse(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), CoinsDiscountEffect.class));
		effectManager.leaderEffectManage(game.getLeaderCards().get(18).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(18), mainGC);
		assertTrue(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), CoinsDiscountEffect.class));
	}

	@Test
	public void testDoubleResourceEffect() throws IOException {
		assertFalse(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), DoubleResourceEffect.class));
		effectManager.leaderEffectManage(game.getLeaderCards().get(16).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(16), mainGC);
		assertTrue(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), DoubleResourceEffect.class));
	}

	@Test
	public void testInOccupiedSpaceEffect() throws IOException {
		assertFalse(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), InOccupiedSpaceEffect.class));
		effectManager.leaderEffectManage(game.getLeaderCards().get(1).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(1), mainGC);
		assertTrue(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), InOccupiedSpaceEffect.class));
	}

	@Test
	public void testLeaderResourceEffect() throws IOException { // verificare
		assertEquals(0, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith().intValue());
		effectManager.leaderEffectManage(game.getLeaderCards().get(4).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(4), mainGC);
		assertEquals(1, game.getPlayersOrder().get(0).getPersonalBoard().getResources().getFaith().intValue());
	}

	@Test
	public void testMemberBonusEffect() throws IOException {
		assertFalse(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), MemberBonusEffect.class));
		effectManager.leaderEffectManage(game.getLeaderCards().get(11).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(11), mainGC);
		assertTrue(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), MemberBonusEffect.class));
	}

	@Test
	public void testMemberChangeEffect() throws IOException {
		// malatesta
		assertFalse(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), MemberChangeEffect.class));
		effectManager.leaderEffectManage(game.getLeaderCards().get(12).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(12), mainGC);
		assertTrue(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), MemberChangeEffect.class));
		// ludovico il moro
		effectManager.leaderEffectManage(game.getLeaderCards().get(14).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(14), mainGC);
		assertTrue(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), MemberChangeEffect.class));
	}

	@Test
	public void testNoMilitaryRequestEffect() throws IOException {
		assertFalse(
				moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), NoMilitaryRequestEffect.class));
		effectManager.leaderEffectManage(game.getLeaderCards().get(15).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(15), mainGC);
		assertTrue(
				moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), NoMilitaryRequestEffect.class));
	}

	@Test
	public void testNoOccupiedTowerEffect() throws IOException {
		assertFalse(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), NoOccupiedTowerEffect.class));
		effectManager.leaderEffectManage(game.getLeaderCards().get(2).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(2), mainGC);
		assertTrue(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), NoOccupiedTowerEffect.class));
	}
	
	@Test
	public void testCopyEffect() throws IOException {
		assertFalse(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), NoOccupiedTowerEffect.class));
		effectManager.leaderEffectManage(game.getLeaderCards().get(2).getEffect(), game.getPlayersOrder().get(0),
				game.getLeaderCards().get(2), mainGC);
		assertTrue(moveManager.containsClass(game.getPlayersOrder().get(0).getEffects(), NoOccupiedTowerEffect.class));
	}
}
