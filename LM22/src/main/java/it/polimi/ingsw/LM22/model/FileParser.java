package it.polimi.ingsw.LM22.model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class FileParser {

	public void getDevCards(Game game) {
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
				.registerSubtype(NoBoardBonusEffect.class).registerSubtype(WorkBonusEffect.class)
				.registerSubtype(ColorCardBonusEffect.class).registerSubtype(NoCardSpaceBonusEffect.class);

		// tipi di carte per l'oggetto GSON
		Type bType = new TypeToken<ArrayList<BuildingCard>>() {
		}.getType();
		Type cType = new TypeToken<ArrayList<CharacterCard>>() {
		}.getType();
		Type tType = new TypeToken<ArrayList<TerritoryCard>>() {
		}.getType();
		Type vType = new TypeToken<ArrayList<VentureCard>>() {
		}.getType();

		try {
			// chiamo la funzione loadCards che restituisce
			ArrayList<BuildingCard> bCards = f.<ArrayList<BuildingCard>>loadDevCards("BuildingCard", AdapterImm,
					AdapterPerm, bType);
			ArrayList<CharacterCard> cCards = f.<ArrayList<CharacterCard>>loadDevCards("CharacterCard", AdapterImm,
					AdapterPerm, cType);
			ArrayList<TerritoryCard> tCards = f.<ArrayList<TerritoryCard>>loadDevCards("TerritoryCard", AdapterImm,
					AdapterPerm, tType);
			ArrayList<VentureCard> vCards = f.<ArrayList<VentureCard>>loadDevCards("VentureCard", AdapterImm,
					AdapterPerm, vType);
			// importo le carte nell'oggetto game
			game.setBuildingCards(bCards);
			game.setCharacterCards(cCards);
			game.setTerritoryCards(tCards);
			game.setVentureCards(vCards);
		} catch (IOException e) {
			System.err.println("Errore nel caricamento dei file JSON");
		}

		// try {
		// String text = new
		// String(Files.readAllBytes(Paths.get(path)),StandardCharsets.UTF_8);
		// Type tType = new TypeToken<Collection<TerritoryCard>>() {
		// }.getType();
		// System.out.println(tType);
		// Gson tGson = new
		// GsonBuilder().registerTypeAdapterFactory(AdapterImm).create();
		// System.out.println(tGson);
		// Collection<TerritoryCard> tCards = tGson.fromJson(text, tType);
		// System.out.println(tCards);
		// } catch (IOException e) {
		// System.err.println("Errore nell'apertura del file JSON
		// TerritoryCard.json");
		// }

		// for (TerritoryCard c : tCards) {
		// try {
		// System.out.println(
		// c.getName() + " " + ((ResourcePrivilegeEffect)
		// c.getImmediateEffect()).getResource().getStone()
		// + " " + c.getPermanentEffect().getResource().getStone());
		// } catch (Exception e) {
		// }
		// }
	}

	private <T> T loadDevCards(String fileName, RuntimeTypeAdapterFactory<ImmediateEffect> AdapterImm,
			RuntimeTypeAdapterFactory<PermanentEffect> AdapterPerm, Type type) throws IOException {
		// desktop come impostazione predefinita ###
		String desktop = System.getProperty("user.home") + "\\Desktop\\";
		// ottengo il contenuto del file
		String text = new String(Files.readAllBytes(Paths.get(desktop + fileName + ".json")), StandardCharsets.UTF_8);
		// genero l'oggetto deserializzatore GSON
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(AdapterImm).registerTypeAdapterFactory(AdapterPerm)
				.create();
		// ritorno le carte con il tipo generico T (specificato alla chiamata)
		T cards = gson.fromJson(text, type);
		return cards;
	}

}