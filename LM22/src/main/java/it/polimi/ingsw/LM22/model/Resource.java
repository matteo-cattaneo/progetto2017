package it.polimi.ingsw.LM22.model;

import java.util.HashMap;

public class Resource {
	//creare una enum per tutti i tipi di risorsa?
		/*
		 * WOOD
		 * STONE
		 * SERVANT
		 * COINS
		 * FIATH
		 * MILITARY
		 * VICTORY
		 * */
	private HashMap<String, Integer> resource;

	public HashMap<String, Integer> getResource() {
		return resource;
	}

	public void setResource(HashMap<String, Integer> resource) {
		this.resource = resource;
	}
}
