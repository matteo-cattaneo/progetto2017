package it.polimi.ingsw.LM22.model;

import java.io.IOException;

import org.junit.Test;

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
