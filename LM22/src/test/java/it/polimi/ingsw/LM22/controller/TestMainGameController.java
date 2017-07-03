package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.ingsw.LM22.network.server.PlayerInfo;
import junit.framework.TestCase;

public class TestMainGameController extends TestCase {
	MainGameController mainGC;

	public void setUp() throws RemoteException {
		ArrayList<PlayerInfo> room = new ArrayList<PlayerInfo>();
		mainGC = new MainGameController(room);
	}

	@Test
	public void testFakeGame() throws IOException {
		mainGC.run();
	}
}
