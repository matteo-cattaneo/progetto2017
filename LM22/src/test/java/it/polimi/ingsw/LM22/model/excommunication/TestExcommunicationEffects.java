
package it.polimi.ingsw.LM22.model.excommunication;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import it.polimi.ingsw.LM22.controller.FileParser;
import it.polimi.ingsw.LM22.model.Game;
import junit.framework.TestCase;

public class TestExcommunicationEffects extends TestCase {
	FileParser fp;
	Game game;
	ExCommunication[] ExComm;

	public void setUp() throws IOException {
		fp = new FileParser();
		game = new Game();
		loadExTile();
	}

	public void loadExTile() throws IOException {
		Type type = new TypeToken<ExCommunication[]>() {
		}.getType();
		RuntimeTypeAdapterFactory<ExEffect> effect = RuntimeTypeAdapterFactory.of(ExEffect.class, "type")
				.registerSubtype(DiceCardMalusEx.class).registerSubtype(DiceMalusEx.class)
				.registerSubtype(DoubleServantsEx.class).registerSubtype(FinalCardCostMalusEx.class)
				.registerSubtype(FinalResourceMalusEx.class).registerSubtype(NoFinalCardPointsEx.class)
				.registerSubtype(NoFirstTurnEx.class).registerSubtype(NoMarketEx.class)
				.registerSubtype(ResourceMalusEx.class).registerSubtype(WorkMalusEx.class);
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(".//JSON//ExCommunication.json")),
				StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(effect).create();
		ExComm = gson.fromJson(text, type);
	}

	@Test
	public void testDiceCardMalusEx() {
		DiceCardMalusEx exTileEffect = (DiceCardMalusEx) ExComm[7].getEffect();
		assertEquals(4, exTileEffect.getMalus().intValue());
		assertEquals(0, exTileEffect.getCardType().intValue());
		assertEquals("You get a malus of 4 for the 1 tower", exTileEffect.getInfo());
	}

	@Test
	public void testDiceMalusEx() {
		DiceMalusEx exTileEffect = (DiceMalusEx) ExComm[0].getEffect();
		assertEquals(1, exTileEffect.getMalus().intValue());
		assertEquals("You get a malus of 1 for all your colored Members", exTileEffect.getInfo());
	}

	@Test
	public void testDoubleServantsEx() {
		DoubleServantsEx exTileEffect = (DoubleServantsEx) ExComm[12].getEffect();
		assertEquals("You have to give 2 servants to have a bonus of 1", exTileEffect.getInfo());
	}

	@Test
	public void testFinalCardCostMalusEx() {
		FinalCardCostMalusEx exTileEffect = (FinalCardCostMalusEx) ExComm[20].getEffect();
		assertNotNull(exTileEffect.getResource());
		assertEquals(2, exTileEffect.getCardType().intValue());
		assertEquals("You get a malus of wood: 1 stone: 1 servants: 1 coins: 1  due to your 3 cost",
				exTileEffect.getInfo());
	}

	@Test
	public void testFinalResourceMalusEx() {
		FinalResourceMalusEx exTileEffect = (FinalResourceMalusEx) ExComm[17].getEffect();
		assertNotNull(exTileEffect.getResource());
		assertEquals("You get a malus of victory: 5 ", exTileEffect.getInfo());
	}

	@Test
	public void testNoFinalCardPointsEx() {
		NoFinalCardPointsEx exTileEffect = (NoFinalCardPointsEx) ExComm[14].getEffect();
		assertEquals(0, exTileEffect.getCardType().intValue());
		assertEquals("You don't get your final Points for the cards you have of the 1  tower", exTileEffect.getInfo());
	}

	@Test
	public void testNoFirstTurnEx() {
		NoFirstTurnEx exTileEffect = (NoFirstTurnEx) ExComm[13].getEffect();
		assertEquals("You cannot do your first turn of each round, you will recover it at the end of the round",
				exTileEffect.getInfo());
	}

	@Test
	public void testNoMarketEx() {
		NoMarketEx exTileEffect = (NoMarketEx) ExComm[11].getEffect();
		assertEquals("You won't be able anymore to do Market Move", exTileEffect.getInfo());
	}

	@Test
	public void testResourceMalusEx() {
		ResourceMalusEx exTileEffect = (ResourceMalusEx) ExComm[1].getEffect();
		assertNotNull(exTileEffect.getMalus());
		assertEquals("Everytime you earn resources you earn servants: 1  less", exTileEffect.getInfo());
	}

	@Test
	public void testWorkMalusEx() {
		WorkMalusEx exTileEffect = (WorkMalusEx) ExComm[5].getEffect();
		assertEquals("PRODUCTION", exTileEffect.getTypeOfWork());
		assertEquals(3, exTileEffect.getValueOfMalus().intValue());
		assertEquals("You will have a malus of 3 for PRODUCTION actions", exTileEffect.getInfo());
	}
}
