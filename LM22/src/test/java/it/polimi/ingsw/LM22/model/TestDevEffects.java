package it.polimi.ingsw.LM22.model;

import java.io.IOException;

import org.junit.Test;

import it.polimi.ingsw.LM22.controller.FileParser;
import junit.framework.TestCase;

public class TestDevEffects extends TestCase {
	FileParser fp;
	Game game;

	public void setUp() throws IOException {
		fp = new FileParser();
		game = new Game();
		fp.getDevCards(game);
	}

	@Test
	public void testResourcePrivilegeEffect() {
		ResourcePrivilegeEffect effect = (ResourcePrivilegeEffect) game.getBuildingCards().get(1).getImmediateEffect();
		assertEquals(0, effect.getCouncilPrivilege().intValue());
		assertNotNull(effect.getResource());
		assertEquals("You earn%nvictory: 3%n", effect.getInfo());
	}

	@Test
	public void testCardActionEffect() {
		CardActionEffect effect = (CardActionEffect) game.getCharacterCards().get(1).getImmediateEffect();
		assertNotNull(effect.getCardDiscount());
		assertEquals(-1, effect.getCardType().intValue());
		assertEquals(0, effect.getCouncilPrivilege().intValue());
		assertEquals(4, effect.getDiceValue().intValue());
		assertNotNull(effect.getResource());
		assertEquals(
				"You can get a card on the tower you choose%nThe action has a value of 4%nYou get a card discount: No resource%nYou get a reward: faith: 1%n",
				effect.getInfo());
	}

	@Test
	public void testCardToResourceEffect() {
		CardToResourceEffect effect = (CardToResourceEffect) game.getBuildingCards().get(2).getPermanentEffect();
		assertEquals("CHARACTER", effect.getCardRequired());
		assertNotNull(effect.getReward());
		assertEquals("You get victory: 1%nfor every CHARACTER card you have%n", effect.getInfo());
	}

	@Test
	public void testChangeEffect() {
		ChangeEffect effect = (ChangeEffect) game.getBuildingCards().get(0).getPermanentEffect();
		assertNotNull(effect.getExchangeEffect1());
		assertEquals("You can exchange coins: 1%n to faith: 1%n", effect.getInfo());
	}

	@Test
	public void testChangeToPrivilegeEffect() {
		ChangeToPrivilegeEffect effect = (ChangeToPrivilegeEffect) game.getBuildingCards().get(6).getPermanentEffect();
		assertEquals(1, effect.getCouncilPrivilege().intValue());
		assertNotNull(effect.getExchangedResource());
		assertEquals("You can change coins: 1%n into 1 councilPrivilege(s)%n", effect.getInfo());
	}

	@Test
	public void testColorCardBonusEffect() {
		ColorCardBonusEffect effect = (ColorCardBonusEffect) game.getCharacterCards().get(2).getPermanentEffect();
		assertEquals(3, effect.getCardType().intValue());
		assertEquals(2, effect.getDiceBonus().intValue());
		assertNotNull(effect.getCardDiscount());
		assertEquals("You have a bonus of 2 on the 4 tower %nYou have a card discount of No resource%n",
				effect.getInfo());
	}

	@Test
	public void testDoubleChangeEffect() {
		DoubleChangeEffect effect = (DoubleChangeEffect) game.getBuildingCards().get(1).getPermanentEffect();
		assertNotNull(effect.getExchangeEffect1());
		assertNotNull(effect.getExchangeEffect2());
		assertEquals("You can exchange wood: 1%n to coins: 3%n OR You can exchange wood: 2%n to coins: 5%n",
				effect.getInfo());
	}

	@Test
	public void testNoCardSpaceBonusEffect() {
		NoCardSpaceBonusEffect effect = (NoCardSpaceBonusEffect) game.getCharacterCards().get(0).getPermanentEffect();
		assertEquals("You can no more earn Bonuses in Card Action Spaces%n", effect.getInfo());
	}

	@Test
	public void testResourceToResourceEffect() {
		ResourceToResourceEffect effect = (ResourceToResourceEffect) game.getCharacterCards().get(17)
				.getImmediateEffect();
		assertNotNull(effect.getRequirement());
		assertNotNull(effect.getReward());
		assertEquals("for every military: 2%nyou have, you earn victory: 1%n", effect.getInfo());
	}

	@Test
	public void testWorkActionEffect() {
		WorkActionEffect effect = (WorkActionEffect) game.getVentureCards().get(23).getImmediateEffect();
		assertEquals("PRODUCTION", effect.getTypeOfWork());
		assertEquals(3, effect.getWorkActionValue().intValue());
		assertEquals(0, effect.getCouncilPrivilege().intValue());
		assertNotNull(effect.getResource());
		assertEquals("You earn No resource%nYou can do a PRODUCTION Action with a value of 3%n", effect.getInfo());
	}

	@Test
	public void testWorkBonusEffect() {
		WorkBonusEffect effect = (WorkBonusEffect) game.getCharacterCards().get(6).getPermanentEffect();
		assertEquals("HARVEST", effect.getTypeOfWork());
		assertEquals(2, effect.getWorkBonusValue().intValue());
		assertEquals("You get a bonus of 2 for HARVEST actions%n", effect.getInfo());
	}
}
