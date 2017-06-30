package it.polimi.ingsw.LM22.model.leader;

import java.io.IOException;

import org.junit.Test;

import it.polimi.ingsw.LM22.controller.FileParser;
import it.polimi.ingsw.LM22.model.Game;

import junit.framework.TestCase;

public class TestLeaderEffects extends TestCase {
	FileParser fp;
	Game game;

	public void setUp() throws IOException {
		fp = new FileParser();
		game = new Game();
		fp.getLeaderCards(game);
	}

	@Test
	public void testChurchSubstainEffect() {
		ChurchSubstainEffect effect = (ChurchSubstainEffect) game.getLeaderCards().get(10).getEffect();
		assertNotNull(effect.getReward());
		assertEquals("Everytime you substain the Church you will earn victory: 5%n", effect.getInfo());
	}

	@Test
	public void testCoinsDiscountEffect() {
		CoinsDiscountEffect effect = (CoinsDiscountEffect) game.getLeaderCards().get(18).getEffect();
		assertNotNull(effect.getDiscount());
		assertEquals("Everytime you want a development card you will get a discount on its cost of coins: 3%n",
				effect.getInfo());
	}

	@Test
	public void testCopyEffect() {
		CopyEffect effect = (CopyEffect) game.getLeaderCards().get(13).getEffect();
		assertEquals("You can copy the effect of a Leader Card already played by another Player of your game%n"
				+ "once you have chosen the effect is no more changeable%n", effect.getInfo());
	}

	@Test
	public void testDoubleResourceEffect() {
		DoubleResourceEffect effect = (DoubleResourceEffect) game.getLeaderCards().get(16).getEffect();
		assertNotNull(effect.getResourceMoltiplicator());
		assertEquals(
				"Everytime you earn resources due to an immediate effect of a development card you earn "
						+ "the reward moltiplicated by wood: 2%nstone: 2%nservants: 2%ncoins: 2%nfaith: 1%nmilitary: 1%nvictory: 1%n",
				effect.getInfo());
	}

	@Test
	public void testInOccupiedSpaceEffect() {
		InOccupiedSpaceEffect effect = (InOccupiedSpaceEffect) game.getLeaderCards().get(1).getEffect();
		assertEquals("You will be able to put your family members in spaces already occupied%n", effect.getInfo());
	}

	@Test
	public void testLeaderResourceEffect() {
		LeaderResourceEffect effect = (LeaderResourceEffect) game.getLeaderCards().get(4).getEffect();
		assertEquals(0, effect.getCouncilPrivilege().intValue());
		assertNotNull(effect.getResource());
		assertEquals("You will get faith: 1%n", effect.getInfo());
	}

	@Test
	public void testMemberBonusEffect() {
		MemberBonusEffect effect = (MemberBonusEffect) game.getLeaderCards().get(11).getEffect();
		assertEquals("ALL", effect.getTypeOfMember());
		assertEquals(2, effect.getValueOfBonus().intValue());
		assertEquals("You will get a bonus of 2 for ALL member%n", effect.getInfo());
	}

	@Test
	public void testMemberChangeEffect() {
		MemberChangeEffect effect = (MemberChangeEffect) game.getLeaderCards().get(3).getEffect();
		assertEquals("COLORED", effect.getTypeOfMember());
		assertEquals(6, effect.getNewValueOfMember().intValue());
		assertEquals("You will get a 6 for COLORED member%n", effect.getInfo());
	}

	@Test
	public void testNoMilitaryRequestEffect() {
		NoMilitaryRequestEffect effect = (NoMilitaryRequestEffect) game.getLeaderCards().get(15).getEffect();
		assertEquals("You won't have no more Military points requests to get Territory cards%n", effect.getInfo());
	}

	@Test
	public void testNoOccupiedTowerEffect() {
		NoOccupiedTowerEffect effect = (NoOccupiedTowerEffect) game.getLeaderCards().get(2).getEffect();
		assertEquals("If you go on an occupied tower you won't have to pay the additional cost%n", effect.getInfo());
	}

	@Test
	public void testWorkAction() {
		WorkAction effect = (WorkAction) game.getLeaderCards().get(0).getEffect();
		assertEquals("HARVEST", effect.getTypeOfWork());
		assertEquals(1, effect.getValueOfWork().intValue());
		assertEquals("You are able to do a HARVEST action with a value of 1%n", effect.getInfo());
	}
}
