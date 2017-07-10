package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import it.polimi.ingsw.LM22.network.client.IClient;
import it.polimi.ingsw.LM22.network.server.IPlayer;
import it.polimi.ingsw.LM22.network.server.PlayerInfo;
import junit.framework.TestCase;

public class TestMainGameController extends TestCase {
	MainGameController mainGC;

	public void setUp() throws IOException {
		ArrayList<PlayerInfo> room = new ArrayList<PlayerInfo>();
		PlayerInfo p1 = new PlayerInfo();
		p1.setName("Player1");
		p1.setIplayer((IPlayer) new NetworkTest(false));
		room.add(p1);
		// provo la disconnessione del secondo player
		PlayerInfo p2 = new PlayerInfo();
		p2.setName("Player2");
		p2.setIplayer((IPlayer) new NetworkTest(true));
		room.add(p2);
		mainGC = new MainGameController(room);
	}

	@Test
	public void testFakeGame() throws IOException {
		mainGC.run();
	}
}

class NetworkTest implements IPlayer {
	Game game;
	boolean secondPlayer;

	public NetworkTest(boolean secondPlayer) {
		this.secondPlayer = secondPlayer;
	}

	@Override
	public String yourTurn() throws IOException {
		if (secondPlayer)
			return "End@Disconnect@";
		else
			return "End@";
	}

	@Override
	public void showBoard(Game game) throws IOException {
		this.game = game;
	}

	@Override
	public void login(IClient client) throws RemoteException {
	}

	@Override
	public String getName() throws RemoteException {
		return null;
	}

	@Override
	public String councilRequest(Integer number) throws IOException {
		return null;
	}

	@Override
	public String servantsRequest() throws IOException {
		return null;
	}

	@Override
	public String towerRequest() throws IOException {
		return null;
	}

	@Override
	public String floorRequest() throws IOException {
		return null;
	}

	@Override
	public boolean supportRequest() throws IOException {
		return false;
	}

	@Override
	public String colorRequest() throws IOException {
		return null;
	}

	@Override
	public Integer ventureCostRequest(VentureCard vc) throws IOException {
		return null;
	}

	@Override
	public boolean changeRequest(Resource[] exchange) throws IOException {
		return false;
	}

	@Override
	public Integer doubleChangeRequest(DoubleChangeEffect effect) throws IOException {
		return null;
	}

	@Override
	public String askToPlayerForEffectToCopy(List<LeaderCard> lcards) throws IOException {
		return null;
	}

	@Override
	public void showMsg(String msg) throws IOException {
	}

	@Override
	public Integer selectPersonalTile(Game game) throws IOException {
		return 1;
	}

	@Override
	public void selectLeaderCard(Game game) throws IOException {
		this.game = game;
	}

	@Override
	public String getLeaderCard() throws IOException {
		return game.getPlayersOrder().get(0).getLeaderCards().get(0).getName();
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public String getPassword() throws IOException {
		return null;
	}

	@Override
	public boolean changeRequest(Resource exchange, Integer privileges) throws IOException {
		return false;
	}

}
