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
		
		try {
			text = new String(Files.readAllBytes(Paths.get(path + "TerritoryCard.json")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Type collectionType = new TypeToken<Collection<TerritoryCard>>() {
		}.getType();

		RuntimeTypeAdapterFactory<IEffect> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
				.of(IEffect.class, "type").registerSubtype(ResourcePrivilegeEffect.class);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
		Collection<TerritoryCard> tCards = gson.fromJson(text, collectionType);

		for (TerritoryCard c : tCards) {
			try {
				System.out.print(c.getName() + " " + ((ResourcePrivilegeEffect) c.getImmediateEffect()).getResource().getStone());
				System.out.println(" " + c.getPermanentEffect().getResource().getStone());

			} catch (Exception e) {
			}
		}
	}

}
