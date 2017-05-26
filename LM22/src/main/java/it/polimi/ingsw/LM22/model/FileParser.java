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
		String text = null;
		String path = System.getProperty("user.home") + "\\Desktop\\";

		RuntimeTypeAdapterFactory<ImmediateEffect> AdapterImm = RuntimeTypeAdapterFactory
				.of(ImmediateEffect.class, "type").registerSubtype(ResourcePrivilegeEffect.class)
				.registerSubtype(WorkActionEffect.class).registerSubtype(ResourceToResourceEffect.class)
				.registerSubtype(CardToResourceEffect.class).registerSubtype(NoEffect.class)
				.registerSubtype(ChangeEffect.class).registerSubtype(CardActionEffect.class);
		RuntimeTypeAdapterFactory<PermanentEffect> AdapterPerm = RuntimeTypeAdapterFactory
				.of(PermanentEffect.class, "type").registerSubtype(NoPermanentEffect.class)
				.registerSubtype(NoBoardBonusEffect.class).registerSubtype(WorkBonusEffect.class)
				.registerSubtype(ColorCardBonusEffect.class).registerSubtype(NoCardSpaceBonusEffect.class);

		try {
			text = new String(Files.readAllBytes(Paths.get(path + "TerritoryCard.json")), StandardCharsets.UTF_8);
			Type tType = new TypeToken<Collection<TerritoryCard>>() {
			}.getType();

			Gson tGson = new GsonBuilder().registerTypeAdapterFactory(AdapterImm).create();
			Collection<TerritoryCard> tCards = tGson.fromJson(text, tType);
		} catch (IOException e) {
			System.err.println("Errore nell'apertura del file JSON TerritoryCard.json");
		}

		// for (TerritoryCard c : tCards) {
		// try {
		// System.out.print(c.getName() + " "
		// + ((ResourcePrivilegeEffect)
		// c.getImmediateEffect()).getResource().getStone());
		// System.out.println(" " +
		// c.getPermanentEffect().getResource().getStone());
		//
		// } catch (Exception e) {
		// }
		// }
		//
		// System.out.println();

		try {
			text = new String(Files.readAllBytes(Paths.get(path + "CharacterCard.json")), StandardCharsets.UTF_8);
			Type cType = new TypeToken<Collection<CharacterCard>>() {
			}.getType();

			Gson cGson = new GsonBuilder().registerTypeAdapterFactory(AdapterImm)
					.registerTypeAdapterFactory(AdapterPerm).create();
			Collection<CharacterCard> cCards = cGson.fromJson(text, cType);
		} catch (IOException e) {
			System.err.println("Errore nell'apertura del file JSON CharacterCard.json");
		}

		// for (CharacterCard c : cCards) {
		// try {
		// System.out.println(c.getName() + " "// + ((NoEffect)
		// // c.getImmediateEffect())
		// + " " + ((ColorCardBonusEffect)
		// c.getPermanentEffect()).getCardType());
		//
		// } catch (Exception e) {
		// }
		// }

		try {
			text = new String(Files.readAllBytes(Paths.get(path + "BuildingCard.json")), StandardCharsets.UTF_8);
			Type bType = new TypeToken<Collection<BuildingCard>>() {
			}.getType();

			Gson bGson = new GsonBuilder().registerTypeAdapterFactory(AdapterImm).create();
			Collection<BuildingCard> bCards = bGson.fromJson(text, bType);
		} catch (IOException e) {
			System.err.println("Errore nell'apertura del file JSON BuildingCard.json");
		}

		try {
			text = new String(Files.readAllBytes(Paths.get(path + "VentureCard.json")), StandardCharsets.UTF_8);
			Type bType = new TypeToken<Collection<VentureCard>>() {
			}.getType();

			Gson bGson = new GsonBuilder().registerTypeAdapterFactory(AdapterImm).create();
			Collection<VentureCard> bCards = bGson.fromJson(text, bType);
		} catch (IOException e) {
			System.err.println("Errore nell'apertura del file JSON VentureCard.json");
		}
	}

}
