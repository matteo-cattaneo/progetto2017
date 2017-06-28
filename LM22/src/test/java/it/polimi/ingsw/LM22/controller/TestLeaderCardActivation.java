package it.polimi.ingsw.LM22.controller;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import junit.framework.TestCase;

public class TestLeaderCardActivation extends TestCase {
	LeaderCardActivation leaderCardActivation;

	@Test
	public void testLeaderCardActivation() {
		Player p = new Player("name", "color");
		LeaderCard ld = new LeaderCard();
		leaderCardActivation = new LeaderCardActivation(p, ld);
		assertEquals(p, leaderCardActivation.getPlayer());
		assertEquals(ld, leaderCardActivation.getLeaderCard());
	}
}
