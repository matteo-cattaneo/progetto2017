package it.polimi.ingsw.LM22.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CardActionEffect;
import it.polimi.ingsw.LM22.model.CardToResourceEffect;
import it.polimi.ingsw.LM22.model.ChangeEffect;
import it.polimi.ingsw.LM22.model.ChangeToPrivilegeEffect;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.ColorCardBonusEffect;
import it.polimi.ingsw.LM22.model.CouncilSpace;
import it.polimi.ingsw.LM22.model.DoubleChangeEffect;
import it.polimi.ingsw.LM22.model.FaithGrid;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.ImmediateEffect;
import it.polimi.ingsw.LM22.model.MarketSpace;
import it.polimi.ingsw.LM22.model.NoCardSpaceBonusEffect;
import it.polimi.ingsw.LM22.model.NoEffect;
import it.polimi.ingsw.LM22.model.NoPermanentEffect;
import it.polimi.ingsw.LM22.model.PermanentEffect;
import it.polimi.ingsw.LM22.model.PersonalBonusTile;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.ResourcePrivilegeEffect;
import it.polimi.ingsw.LM22.model.ResourceToResourceEffect;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.WorkActionEffect;
import it.polimi.ingsw.LM22.model.WorkBonusEffect;
import it.polimi.ingsw.LM22.model.excommunication.DiceCardMalusEx;
import it.polimi.ingsw.LM22.model.excommunication.DiceMalusEx;
import it.polimi.ingsw.LM22.model.excommunication.DoubleServantsEx;
import it.polimi.ingsw.LM22.model.excommunication.ExCommunication;
import it.polimi.ingsw.LM22.model.excommunication.ExEffect;
import it.polimi.ingsw.LM22.model.excommunication.FinalCardCostMalusEx;
import it.polimi.ingsw.LM22.model.excommunication.FinalResourceMalusEx;
import it.polimi.ingsw.LM22.model.excommunication.NoFinalCardPointsEx;
import it.polimi.ingsw.LM22.model.excommunication.NoFirstTurnEx;
import it.polimi.ingsw.LM22.model.excommunication.NoMarketEx;
import it.polimi.ingsw.LM22.model.excommunication.ResourceMalusEx;
import it.polimi.ingsw.LM22.model.excommunication.WorkMalusEx;
import it.polimi.ingsw.LM22.model.leader.CardRequest;
import it.polimi.ingsw.LM22.model.leader.ChurchSubstainEffect;
import it.polimi.ingsw.LM22.model.leader.CoinsDiscountEffect;
import it.polimi.ingsw.LM22.model.leader.CopyEffect;
import it.polimi.ingsw.LM22.model.leader.DoubleResourceEffect;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.model.leader.LeaderCard;
import it.polimi.ingsw.LM22.model.leader.LeaderCardRequest;
import it.polimi.ingsw.LM22.model.leader.LeaderEffect;
import it.polimi.ingsw.LM22.model.leader.LeaderResourceEffect;
import it.polimi.ingsw.LM22.model.leader.MemberBonusEffect;
import it.polimi.ingsw.LM22.model.leader.MemberChangeEffect;
import it.polimi.ingsw.LM22.model.leader.NoMilitaryRequestEffect;
import it.polimi.ingsw.LM22.model.leader.NoOccupiedTowerEffect;
import it.polimi.ingsw.LM22.model.leader.ResourceCardRequest;
import it.polimi.ingsw.LM22.model.leader.ResourceRequest;
import it.polimi.ingsw.LM22.model.leader.WorkAction;

public class FileParser {
	private static final String JSONPATH = ".//JSON//";
	private static final Integer NUM_EXTILE = 7;

	/**
	 * carica da file le carte sviluppo
	 */
	public void getDevCards(Game game) throws IOException {
		FileParser f = new FileParser();
		// definisco i sotto tipi per gli effetti immediati e parmanenti
		RuntimeTypeAdapterFactory<ImmediateEffect> adapterImm = RuntimeTypeAdapterFactory
				.of(ImmediateEffect.class, "type").registerSubtype(ResourcePrivilegeEffect.class)
				.registerSubtype(WorkActionEffect.class).registerSubtype(ResourceToResourceEffect.class)
				.registerSubtype(CardToResourceEffect.class).registerSubtype(NoEffect.class)
				.registerSubtype(ChangeEffect.class).registerSubtype(CardActionEffect.class)
				.registerSubtype(DoubleChangeEffect.class).registerSubtype(ChangeToPrivilegeEffect.class);
		RuntimeTypeAdapterFactory<PermanentEffect> adapterPerm = RuntimeTypeAdapterFactory
				.of(PermanentEffect.class, "type").registerSubtype(NoPermanentEffect.class)
				.registerSubtype(WorkBonusEffect.class).registerSubtype(ColorCardBonusEffect.class)
				.registerSubtype(NoCardSpaceBonusEffect.class);

		// tipi di carte per l'oggetto GSON
		Type bType = new TypeToken<ArrayList<BuildingCard>>() {
		}.getType();
		Type cType = new TypeToken<ArrayList<CharacterCard>>() {
		}.getType();
		Type tType = new TypeToken<ArrayList<TerritoryCard>>() {
		}.getType();
		Type vType = new TypeToken<ArrayList<VentureCard>>() {
		}.getType();

		// chiamo la funzione loadCards che restituisce
		ArrayList<BuildingCard> bCards = f.<ArrayList<BuildingCard>>loadDevCards("BuildingCard", adapterImm,
				adapterPerm, bType);
		ArrayList<CharacterCard> cCards = f.<ArrayList<CharacterCard>>loadDevCards("CharacterCard", adapterImm,
				adapterPerm, cType);
		ArrayList<TerritoryCard> tCards = f.<ArrayList<TerritoryCard>>loadDevCards("TerritoryCard", adapterImm,
				adapterPerm, tType);
		ArrayList<VentureCard> vCards = f.<ArrayList<VentureCard>>loadDevCards("VentureCard", adapterImm, adapterPerm,
				vType);
		// importo le carte nell'oggetto game
		game.setBuildingCards(bCards);
		game.setCharacterCards(cCards);
		game.setTerritoryCards(tCards);
		game.setVentureCards(vCards);
	}

	private <T> T loadDevCards(String fileName, RuntimeTypeAdapterFactory<ImmediateEffect> adapterImm,
			RuntimeTypeAdapterFactory<PermanentEffect> adapterPerm, Type type) throws IOException {
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + fileName + ".json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapterImm).registerTypeAdapterFactory(adapterPerm)
				.create();
		// ritorno le carte con il tipo generico T (specificato alla chiamata)
		return gson.fromJson(text, type);
	}

	/**
	 * carica da file le carte leader
	 */
	public void getLeaderCards(Game game) throws IOException {
		RuntimeTypeAdapterFactory<LeaderCardRequest> req = RuntimeTypeAdapterFactory.of(LeaderCardRequest.class, "type")
				.registerSubtype(CardRequest.class).registerSubtype(ResourceRequest.class)
				.registerSubtype(ResourceCardRequest.class);
		RuntimeTypeAdapterFactory<LeaderEffect> effect = RuntimeTypeAdapterFactory.of(LeaderEffect.class, "type")
				.registerSubtype(WorkAction.class).registerSubtype(LeaderResourceEffect.class)
				.registerSubtype(CopyEffect.class).registerSubtype(NoOccupiedTowerEffect.class)
				.registerSubtype(InOccupiedSpaceEffect.class).registerSubtype(NoMilitaryRequestEffect.class)
				.registerSubtype(CoinsDiscountEffect.class).registerSubtype(DoubleResourceEffect.class)
				.registerSubtype(ChurchSubstainEffect.class).registerSubtype(MemberChangeEffect.class)
				.registerSubtype(MemberBonusEffect.class);
		Type lType = new TypeToken<ArrayList<LeaderCard>>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + "LeaderCard.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(req).registerTypeAdapterFactory(effect).create();
		ArrayList<LeaderCard> lCards = gson.fromJson(text, lType);
		game.setLeaderCards(lCards);
	}

	/**
	 * carica da file i bonus del palazzo del consiglio
	 */
	public void getCouncilSpace(Game game) throws IOException {
		Type type = new TypeToken<CouncilSpace>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + "CouncilSpace.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		CouncilSpace councilPalace = gson.fromJson(text, type);
		game.getBoardgame().setCouncilPalace(councilPalace);
	}

	/**
	 * carica da file i bonus del mercato
	 */
	public void getMarketSpace(Game game) throws IOException {
		Type type = new TypeToken<MarketSpace[]>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + "MarketSpace.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		MarketSpace[] marketSpace = gson.fromJson(text, type);
		game.getBoardgame().setMarket(marketSpace);
	}

	/**
	 * carica da file i bonus del tracciato dei punti fede
	 */
	public void getFaithGrid(Game game) throws IOException {
		Type type = new TypeToken<Resource[]>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + "FaithGridReward.json")),
				StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		Resource[] resources = gson.fromJson(text, type);
		FaithGrid faithGrid = new FaithGrid();
		faithGrid.setRewards(resources);
		game.getBoardgame().setFaithGrid(faithGrid);
	}

	/**
	 * carica da file le tessere bonus personale (solo avanzate)
	 */
	public void getPersonalBonusTile(Game game) throws IOException {
		Type type = new TypeToken<PersonalBonusTile[]>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + "PersonalBonusTile.json")),
				StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		PersonalBonusTile[] personalBonusTile = gson.fromJson(text, type);
		game.setPersonalBonusTile(personalBonusTile);
	}

	/**
	 * carica da file i bonus degli spazi azione delle torri
	 */
	public void getCardSpace(Game game) throws IOException {
		Type type = new TypeToken<Tower[]>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + "CardSpace.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		Tower[] towers = gson.fromJson(text, type);
		game.getBoardgame().setTowers(towers);
	}

	/**
	 * carica da file le tessere scomunica e ne sceglie 3 casualmente
	 */
	public void getExCommunicationsTile(Game game) throws IOException {
		Type type = new TypeToken<ExCommunication[]>() {
		}.getType();
		RuntimeTypeAdapterFactory<ExEffect> effect = RuntimeTypeAdapterFactory.of(ExEffect.class, "type")
				.registerSubtype(DiceCardMalusEx.class).registerSubtype(DiceMalusEx.class)
				.registerSubtype(DoubleServantsEx.class).registerSubtype(FinalCardCostMalusEx.class)
				.registerSubtype(FinalResourceMalusEx.class).registerSubtype(NoFinalCardPointsEx.class)
				.registerSubtype(NoFirstTurnEx.class).registerSubtype(NoMarketEx.class)
				.registerSubtype(ResourceMalusEx.class).registerSubtype(WorkMalusEx.class);
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + "ExCommunication.json")),
				StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(effect).create();
		ExCommunication[] exComm = gson.fromJson(text, type);
		for (int i = 0; i < 3; i++) {
			Random r = new Random();
			game.getBoardgame().getFaithGrid().getExCommunicationTiles()
					.add(exComm[r.nextInt(NUM_EXTILE) + i * NUM_EXTILE]);
		}
	}

	/**
	 * carica da file il timeout di una mossa di ogni giocatore
	 */
	public void getMoveTimeouts(Game game) throws IOException {
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + "Timeouts.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		JsonObject jobj = new Gson().fromJson(text, JsonObject.class);
		game.setMoveTimer(Integer.parseInt(jobj.get("move").toString()));
	}

	/**
	 * carica da file il timeout della creazione di una room
	 */
	public static Integer getLoginTimeouts() throws IOException {
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONPATH + "Timeouts.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		JsonObject jobj = new Gson().fromJson(text, JsonObject.class);
		return Integer.parseInt(jobj.get("login").toString());
	}
}