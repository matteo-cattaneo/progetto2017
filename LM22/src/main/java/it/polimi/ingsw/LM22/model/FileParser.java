package it.polimi.ingsw.LM22.model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class FileParser {

	public static void main(String[] args) {
		Game game = new Game();
		FileParser f = new FileParser();
		f.getDevCards(game);
	}

	public void getDevCards(Game game) {
		FileParser f = new FileParser();
		// define subtypes for Immediate and Permament effect
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

		Collection<BuildingCard> bCards = null;
		Collection<CharacterCard> cCards = null;
		Collection<TerritoryCard> tCards = null;
		Collection<VentureCard> vCards = null;

		// card type for GSON method
		Type bType = new TypeToken<Collection<BuildingCard>>() {
		}.getType();
		Type cType = new TypeToken<Collection<CharacterCard>>() {
		}.getType();
		Type tType = new TypeToken<Collection<TerritoryCard>>() {
		}.getType();
		Type vType = new TypeToken<Collection<VentureCard>>() {
		}.getType();

		try {
			// call loadCards function that return specific card collection
			bCards = f.<Collection<BuildingCard>>loadCards("BuildingCard", AdapterImm, AdapterPerm, bType);
			cCards = f.<Collection<CharacterCard>>loadCards("CharacterCard", AdapterImm, AdapterPerm, cType);
			tCards = f.<Collection<TerritoryCard>>loadCards("TerritoryCard", AdapterImm, AdapterPerm, tType);
			vCards = f.<Collection<VentureCard>>loadCards("VentureCard", AdapterImm, AdapterPerm, vType);
			// set cards into game object
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

		for (TerritoryCard c : tCards) {
			try {
				System.out.println(
						c.getName() + " " + ((ResourcePrivilegeEffect) c.getImmediateEffect()).getResource().getStone()
								+ " " + c.getPermanentEffect().getResource().getStone());
			} catch (Exception e) {
			}
		}
	}

	private <T> T loadCards(String fileName, RuntimeTypeAdapterFactory<ImmediateEffect> AdapterImm,
			RuntimeTypeAdapterFactory<PermanentEffect> AdapterPerm, Type type) throws IOException {
		// default JSON location
		String desktop = System.getProperty("user.home") + "\\Desktop\\";
		// get file content
		String text = new String(Files.readAllBytes(Paths.get(desktop + fileName + ".json")), StandardCharsets.UTF_8);
		// generate the serializer GSON object
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(AdapterImm).registerTypeAdapterFactory(AdapterPerm)
				.create();
		// obtain the cards structure using the T generic type
		T cards = gson.fromJson(text, type);
		return cards;
	}

}