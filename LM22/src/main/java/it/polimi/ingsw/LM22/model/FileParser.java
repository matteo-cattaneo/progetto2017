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
		FileParser f = new FileParser();
		String path = null;
		Type type;
		// default JSON location
		String desktop = System.getProperty("user.home") + "\\Desktop\\";
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

		try {
			// JSON file path
			path = desktop + "TerritoryCard.json";
			// card type for GSON
			type = new TypeToken<Collection<TerritoryCard>>() {
			}.getType();
			// specific card collection
			Collection<TerritoryCard> tCards = f.<TerritoryCard>loadCards(path, AdapterImm, AdapterPerm, type);

			path = desktop + "CharacterCard.json";
			type = new TypeToken<Collection<CharacterCard>>() {
			}.getType();
			Collection<CharacterCard> cCards = f.<CharacterCard>loadCards(path, AdapterImm, AdapterPerm, type);

			path = desktop + "BuildingCard.json";
			type = new TypeToken<Collection<BuildingCard>>() {
			}.getType();
			Collection<BuildingCard> bCards = f.<BuildingCard>loadCards(path, AdapterImm, AdapterPerm, type);

			path = desktop + "VentureCard.json";
			type = new TypeToken<Collection<VentureCard>>() {
			}.getType();
			Collection<VentureCard> vCards = f.<VentureCard>loadCards(path, AdapterImm, AdapterPerm, type);
		} catch (IOException e) {
			System.err.println("Errore nell'apertura del file JSON " + path);
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
		// System.out.print(c.getName() + " "
		// + ((ResourcePrivilegeEffect)
		// c.getImmediateEffect()).getResource().getStone());
		// System.out.println(" " +
		// c.getPermanentEffect().getResource().getStone());
		// } catch (Exception e) {
		// }
		// }
	}

	private <T> Collection<T> loadCards(String path, RuntimeTypeAdapterFactory<ImmediateEffect> AdapterImm,
			RuntimeTypeAdapterFactory<PermanentEffect> AdapterPerm, Type type) throws IOException {
		// get file content
		String text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		// generate the serializer GSON object
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(AdapterImm).registerTypeAdapterFactory(AdapterPerm)
				.create();
		// obtain the cards structure using the T generic type
		Collection<T> cards = gson.fromJson(text, type);
		return cards;
	}

}
