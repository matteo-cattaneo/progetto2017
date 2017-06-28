package it.polimi.ingsw.LM22.model;

import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import junit.framework.TestCase;

public class TestGame extends TestCase {
	Game game = new Game();

	@Test
	public void testGame() {
		game.setMoveTimer(10);
		assertEquals(10, game.getMoveTimer());

		game.setPeriod(1);
		assertEquals(1, game.getPeriod().intValue());

		game.setRound(1);
		assertEquals(1, game.getRound().intValue());

		PersonalBonusTile pbt[] = new PersonalBonusTile[4];
		game.setPersonalBonusTile(pbt);
		assertEquals(pbt, game.getPersonalBonusTile());

		Player players[] = new Player[4];
		game.setPlayers(players);
		assertEquals(players, game.getPlayers());

		ArrayList<Player> playerList = new ArrayList<Player>();
		game.setPlayersOrder(playerList);
		assertEquals(playerList, game.getPlayersOrder());

		ArrayList<BuildingCard> building = new ArrayList<BuildingCard>();
		game.setBuildingCards(building);
		assertEquals(building, game.getBuildingCards());

		ArrayList<CharacterCard> character = new ArrayList<CharacterCard>();
		game.setCharacterCards(character);
		assertEquals(character, game.getCharacterCards());

		ArrayList<TerritoryCard> territory = new ArrayList<TerritoryCard>();
		game.setTerritoryCards(territory);
		assertEquals(territory, game.getTerritoryCards());

		ArrayList<VentureCard> venture = new ArrayList<VentureCard>();
		game.setVentureCards(venture);
		assertEquals(venture, game.getVentureCards());

		ArrayList<LeaderCard> leader = new ArrayList<LeaderCard>();
		game.setLeaderCards(leader);
		assertEquals(leader, game.getLeaderCards());
	}
}
