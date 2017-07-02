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
	private static final String JSONpath = ".//JSON//";
	private static final Integer nExTile = 7;

	public void getDevCards(Game game) throws IOException {
		FileParser f = new FileParser();
		// definisco i sotto tipi per gli effetti immediati e parmanenti
		RuntimeTypeAdapterFactory<ImmediateEffect> AdapterImm = RuntimeTypeAdapterFactory
				.of(ImmediateEffect.class, "type").registerSubtype(ResourcePrivilegeEffect.class)
				.registerSubtype(WorkActionEffect.class).registerSubtype(ResourceToResourceEffect.class)
				.registerSubtype(CardToResourceEffect.class).registerSubtype(NoEffect.class)
				.registerSubtype(ChangeEffect.class).registerSubtype(CardActionEffect.class)
				.registerSubtype(DoubleChangeEffect.class).registerSubtype(ChangeToPrivilegeEffect.class);
		RuntimeTypeAdapterFactory<PermanentEffect> AdapterPerm = RuntimeTypeAdapterFactory
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
		ArrayList<BuildingCard> bCards = f.<ArrayList<BuildingCard>>loadDevCards("BuildingCard", AdapterImm,
				AdapterPerm, bType);
		ArrayList<CharacterCard> cCards = f.<ArrayList<CharacterCard>>loadDevCards("CharacterCard", AdapterImm,
				AdapterPerm, cType);
		ArrayList<TerritoryCard> tCards = f.<ArrayList<TerritoryCard>>loadDevCards("TerritoryCard", AdapterImm,
				AdapterPerm, tType);
		ArrayList<VentureCard> vCards = f.<ArrayList<VentureCard>>loadDevCards("VentureCard", AdapterImm, AdapterPerm,
				vType);
		// importo le carte nell'oggetto game
		game.setBuildingCards(bCards);
		game.setCharacterCards(cCards);
		game.setTerritoryCards(tCards);
		game.setVentureCards(vCards);
	}

	private <T> T loadDevCards(String fileName, RuntimeTypeAdapterFactory<ImmediateEffect> AdapterImm,
			RuntimeTypeAdapterFactory<PermanentEffect> AdapterPerm, Type type) throws IOException {
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + fileName + ".json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(AdapterImm).registerTypeAdapterFactory(AdapterPerm)
				.create();
		// ritorno le carte con il tipo generico T (specificato alla chiamata)
		T cards = gson.fromJson(text, type);
		return cards;
	}

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
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + "LeaderCard.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(req).registerTypeAdapterFactory(effect).create();
		ArrayList<LeaderCard> lCards = gson.fromJson(text, lType);
		game.setLeaderCards(lCards);
	}

	public void getCouncilSpace(Game game) throws IOException {
		Type type = new TypeToken<CouncilSpace>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + "CouncilSpace.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		CouncilSpace councilPalace = gson.fromJson(text, type);
		game.getBoardgame().setCouncilPalace(councilPalace);
	}

	public void getMarketSpace(Game game) throws IOException {
		Type type = new TypeToken<MarketSpace[]>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + "MarketSpace.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		MarketSpace[] marketSpace = gson.fromJson(text, type);
		game.getBoardgame().setMarket(marketSpace);
	}

	public void getFaithGrid(Game game) throws IOException {
		Type type = new TypeToken<Resource[]>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + "FaithGridReward.json")),
				StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		Resource[] resources = gson.fromJson(text, type);
		FaithGrid faithGrid = new FaithGrid();
		faithGrid.setRewards(resources);
		game.getBoardgame().setFaithGrid(faithGrid);
	}

	public void getPersonalBonusTile(Game game) throws IOException {
		Type type = new TypeToken<PersonalBonusTile[]>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + "PersonalBonusTile.json")),
				StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		PersonalBonusTile[] personalBonusTile = gson.fromJson(text, type);
		game.setPersonalBonusTile(personalBonusTile);
	}

	public void getCardSpace(Game game) throws IOException {
		Type type = new TypeToken<Tower[]>() {
		}.getType();
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + "CardSpace.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().create();
		Tower[] towers = gson.fromJson(text, type);
		game.getBoardgame().setTowers(towers);
	}

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
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + "ExCommunication.json")),
				StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(effect).create();
		ExCommunication[] ExComm = gson.fromJson(text, type);
		for (int i = 0; i < 3; i++) {
			Random r = new Random();
			game.getBoardgame().getFaithGrid().getExCommunicationTiles().add(ExComm[r.nextInt(nExTile) + i * nExTile]);
		}
	}

	public void getMoveTimeouts(Game game) throws IOException {
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + "Timeouts.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		JsonObject jobj = new Gson().fromJson(text, JsonObject.class);
		game.setMoveTimer(Integer.parseInt(jobj.get("move").toString()));
	}

	public static Integer getLoginTimeouts() throws IOException {
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(JSONpath + "Timeouts.json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		JsonObject jobj = new Gson().fromJson(text, JsonObject.class);
		return Integer.parseInt(jobj.get("login").toString());
	}
}