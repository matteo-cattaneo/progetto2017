package it.polimi.ingsw.LM22.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import junit.framework.TestCase;

public class TestPlayer extends TestCase {

	Player prova;

	public void setUp() {
		prova = new Player("Nicola", "Green");
	}

	@Test
	public void testConstructor() {
		assertEquals("Nicola", prova.getNickname());
		assertEquals("Green", prova.getColor());

	}

	@Test
	public void testHandLaderCards() {
		List<LeaderCard> list = new ArrayList<LeaderCard>();
		LeaderCard l1 = new LeaderCard();
		LeaderCard l2 = new LeaderCard();
		LeaderCard l3 = new LeaderCard();
		list.add(l1);
		list.add(l2);
		list.add(l3);
		prova.setHandLeaderCards(list);
		assertEquals(list, prova.getHandLeaderCards());
	}

	@Test
	public void testActivatedLeaderCards() {
		List<LeaderCard> list = new ArrayList<LeaderCard>();
		LeaderCard l1 = new LeaderCard();
		LeaderCard l2 = new LeaderCard();
		LeaderCard l3 = new LeaderCard();
		list.add(l1);
		list.add(l2);
		list.add(l3);
		prova.setActivatedLeaderCards(list);
		assertEquals(list, prova.getActivatedLeaderCards());
	}

	@Test
	public void testLeaderCards() {
		List<LeaderCard> list = new ArrayList<LeaderCard>();
		LeaderCard l1 = new LeaderCard();
		LeaderCard l2 = new LeaderCard();
		LeaderCard l3 = new LeaderCard();
		list.add(l1);
		list.add(l2);
		list.add(l3);
		prova.setLeaderCards(list);
		assertEquals(list, prova.getLeaderCards());
	}

	@Test
	public void testFamilyMembers() {
		FamilyMember m1 = new FamilyMember(prova, "Orange");
		FamilyMember m2 = new FamilyMember(prova, "White");
		FamilyMember m3 = new FamilyMember(prova, "Black");
		FamilyMember m4 = new FamilyMember(prova, "Uncolored");
		List<FamilyMember> list = new ArrayList<FamilyMember>();
		list.add(m1);
		list.add(m2);
		list.add(m3);
		list.add(m4);
		prova.setMembers(list);
		assertEquals(list, prova.getMembers());
	}

	@Test
	public void testPlayerLists() {
		assertNotNull(prova.getPersonalBoard());
		assertNotNull(prova.getEffects());
	}
}
