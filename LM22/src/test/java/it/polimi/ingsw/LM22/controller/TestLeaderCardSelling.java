package it.polimi.ingsw.LM22.controller;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import junit.framework.TestCase;

public class TestLeaderCardSelling extends TestCase {
	LeaderCardSelling leaderCardSelling;

	@Test
	public void testLeaderCardSelling() {
		Player p = new Player("name", "color");
		LeaderCard ld = new LeaderCard();
		leaderCardSelling = new LeaderCardSelling(p, ld);
		assertEquals(p, leaderCardSelling.getPlayer());
		assertEquals(ld, leaderCardSelling.getLeaderCard());
	}
}
