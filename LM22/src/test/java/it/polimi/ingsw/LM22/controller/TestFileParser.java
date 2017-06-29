package it.polimi.ingsw.LM22.controller;

import java.io.IOException;

import org.junit.Test;

import it.polimi.ingsw.LM22.controller.FileParser;
import it.polimi.ingsw.LM22.model.Game;
import junit.framework.TestCase;

public class TestFileParser extends TestCase {
	FileParser fp;
	Game game;

	public void setUp() {
		fp = new FileParser();
		game = new Game();
	}

	@SuppressWarnings("static-access")
	@Test
	public void testTimeouts() throws IOException {
		int login = fp.getLoginTimeouts();
		assertTrue(login != 0);

		fp.getMoveTimeouts(game);
		assertTrue(game.getMoveTimer() != 0);
	}
}
