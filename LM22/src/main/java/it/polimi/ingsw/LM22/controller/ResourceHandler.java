package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.Resource;

public class ResourceHandler {
	/*
	 * in questa classe bisognerà tenere conto: degli effetti che danno sia
	 * risorse maggiori --> (vedi santa Rita) oppure delle scomuniche che
	 * tolgono risorse...
	 * 
	 */

	public boolean enoughResources(Resource cardCost, CardMove move, Resource additionalCost, Resource bonus) {
		return true;
	}

	/*
	 * metodo che gestisce il controllo delle carte Venture, sia con doppio
	 * costo che con costo singolo normale o con costo in punti militari
	 */
	public boolean manageVentureCost(CardMove move) {
		return false;

	}

	/*
	 * metodo che controlla se il Player ha sufficienti risorse per comprare una
	 * carta invocato da MoveManager per controllare l'acquistabilità
	 * 
	 * --> sarebbe ottimo avere questo metodo in overload per le varie tipologie
	 * di carte
	 * 
	 */
	public boolean enoughResources(CardMove cardMove, Resource additionalCost) {
		if (!enoughResources(cardMove.getPlayer().getPersonalBoard().getResources(), additionalCost))
			return false;
		return true;
	}

	/*
	 * metodo che controlla, solo con interi essendo carte Character, se il
	 * player in grado di comprare una carta: - è in grado di pagare le
	 * possibili 3 monete in più senza bonus, - è in grado di pagare il costo
	 * della carta anche con i bonus --> se entrambe le condizioni precedente
	 * sono vere allora può effettuare la mossa
	 * 
	 */
	public boolean enoughResources(CharacterCard card, CardMove cardMove, Resource additionalCost, Resource bonus) {
		// Resource playerResource =
		// cardMove.getPlayer().getPersonalBoard().getResources();
		// addResource(playerResource, bonus);
		// if (!enoughResources(playerResource,card.getCost()))
		// return false;
		Integer occupiedCost = additionalCost.getCoins();
		Integer playerCoins = cardMove.getPlayer().getPersonalBoard().getResources().getCoins();
		Integer bonusCoins = bonus.getCoins();
		Integer coinsCost = card.getCost().getCoins();
		if (bonusCoins <= coinsCost) {
			if ((playerCoins - occupiedCost) - (coinsCost - bonusCoins) < 0) {
				return false;
			}
		} else {
			if ((playerCoins - occupiedCost) < 0)
				return false;
		}
		return true;
	}

	/*
	 * DA FARE controllo per i bonus permanenti della carte Personaggio che
	 * hanno l'effetto di diminuire il costo della carta
	 */
	public Resource cardDiscounted(Resource s1, Resource s2) {
		Integer wood = 0;
		Integer stone = 0;
		Integer coins = 0;
		Integer servants = 0;
		Integer faith = 0;
		Integer military = 0;
		Integer victory = 0;
		if (s1.getWood() - s2.getWood() > 0)
			wood = s1.getWood() - s2.getWood();
		if (s1.getStone() - s2.getStone() > 0)
			stone = s1.getStone() - s2.getStone();
		if (s1.getCoins() - s2.getCoins() > 0)
			coins = s1.getCoins() - s2.getCoins();
		if (s1.getServants() - s2.getServants() > 0)
			servants = s1.getServants() - s2.getServants();
		if (s1.getFaith() - s2.getFaith() > 0)
			faith = s1.getFaith() - s2.getFaith();
		if (s1.getMilitary() - s2.getMilitary() > 0)
			military = s1.getMilitary() - s2.getMilitary();
		if (s1.getVictory() - s2.getVictory() > 0)
			victory = s1.getVictory() - s2.getVictory();
		return new Resource(wood, stone, servants, coins, faith, military, victory);
	}

	/*
	 * metodo in grado di controllare se la prima risorsa è >= della seconda
	 * (true)
	 */
	public boolean enoughResources(Resource playerResource, Resource resource) {
		if (playerResource.getWood() < resource.getWood())
			return false;
		if (playerResource.getStone() < resource.getStone())
			return false;
		if (playerResource.getCoins() < resource.getCoins())
			return false;
		if (playerResource.getServants() < resource.getServants())
			return false;
		if (playerResource.getFaith() < resource.getFaith())
			return false;
		if (playerResource.getMilitary() < resource.getMilitary())
			return false;
		if (playerResource.getVictory() < resource.getVictory())
			return false;
		return true;
	}

	/*
	 * aggiunge la risorsa ricevuta come secondo parametro alla risorsa ricevuta
	 * come primo parametro
	 */
	public void addResource(Resource playerResource, Resource addingResource) {
		playerResource.setStone(playerResource.getStone() + addingResource.getStone());
		playerResource.setWood(playerResource.getWood() + addingResource.getWood());
		playerResource.setCoins(playerResource.getCoins() + addingResource.getCoins());
		playerResource.setServants(playerResource.getServants() + addingResource.getServants());
		playerResource.setMilitary(playerResource.getMilitary() + addingResource.getMilitary());
		playerResource.setFaith(playerResource.getFaith() + addingResource.getFaith());
		playerResource.setVictory(playerResource.getVictory() + addingResource.getVictory());
	}

	/*
	 * toglie la risorsa ricevuta come secondo parametro alla risorsa ricevuta
	 * come primo parametro
	 */
	public void subResource(Resource s1, Resource s2) {
		s1.setStone(s1.getStone() - s2.getStone());
		s1.setWood(s1.getWood() - s2.getWood());
		s1.setCoins(s1.getCoins() - s2.getCoins());
		s1.setServants(s1.getServants() - s2.getServants());
		s1.setMilitary(s1.getMilitary() - s2.getMilitary());
		s1.setFaith(s1.getFaith() - s2.getFaith());
		s1.setVictory(s1.getVictory() - s2.getVictory());
	}

	public Resource sumResource(Resource s1, Resource s2) {
		Integer wood = 0;
		Integer stone = 0;
		Integer coins = 0;
		Integer servants = 0;
		Integer faith = 0;
		Integer military = 0;
		Integer victory = 0;
		wood = s1.getWood() + s2.getWood();
		stone = s1.getStone() + s2.getStone();
		coins = s1.getCoins() + s2.getCoins();
		servants = s1.getServants() + s2.getServants();
		faith = s1.getFaith() + s2.getFaith();
		military = s1.getMilitary() + s2.getMilitary();
		victory = s1.getVictory() + s2.getVictory();
		return new Resource(wood, stone, servants, coins, faith, military, victory);
	}
	
	public Resource resourceMultiplication(Resource r, Integer m){
		Resource bonus = new Resource (0,0,0,0,0,0,0);
		Integer wood = r.getWood() * m;
		bonus.setWood(wood);
		Integer stone = r.getStone() * m;
		bonus.setStone(stone);
		Integer coins = r.getCoins() * m;
		bonus.setCoins(coins);
		Integer servants = r.getServants() * m;
		bonus.setServants(servants);
		Integer faith = r.getFaith() * m;
		bonus.setFaith(faith);
		Integer military = r.getMilitary() * m;
		bonus.setMilitary(military);;
		Integer victory = r.getVictory() * m;
		bonus.setVictory(victory);
		return bonus;
	}
}
