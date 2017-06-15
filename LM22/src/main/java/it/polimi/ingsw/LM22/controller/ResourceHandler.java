package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.Effect;
import it.polimi.ingsw.LM22.model.Player;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.excommunication.ResourceMalusEx;

public class ResourceHandler {
	/*
	 * in questa classe bisognerà tenere conto: degli effetti che danno sia
	 * risorse maggiori --> (vedi santa Rita) oppure delle scomuniche che
	 * tolgono risorse...
	 * 
	 */

	/*
	 * metodo che controlla se qualsiasi bonus il player sta prendendo debba essere ridotto
	 */
	public Resource calculateResource(Resource res, Player p) {
		for (Effect e: p.getEffects())
			if (e instanceof ResourceMalusEx){
				res = cardDiscounted(res, ((ResourceMalusEx) e).getMalus());
			}
		return res;
	}
	
	public boolean enoughResources(Resource cardCost, CardMove move, Resource additionalCost, Resource bonus) {
		Resource cost = cardDiscounted(cardCost, bonus);
		if (!enoughResources(diffResource(move.getPlayer().getPersonalBoard().getResources(), move.getServantsAdded()),
				sumResource(additionalCost, cost))) {
			return false;
		}
		return true;
	}

	/*
	 * metodo che gestisce il controllo delle carte Venture, sia con doppio
	 * costo che con costo singolo normale o con costo in punti militari
	 */
	public boolean manageVentureCost(Player p, Resource[] cost2) {
		//TODO
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
		if (!enoughResources(
				diffResource(cardMove.getPlayer().getPersonalBoard().getResources(), cardMove.getServantsAdded()),
				additionalCost))
			return false;
		return true;
	}

	/*
	 * metodo in grado di controllare se la prima risorsa è >= della seconda
	 * (true)
	 */
	public boolean enoughResources(Resource r, Resource resource) {
		if (r.getWood() < resource.getWood())
			return false;
		if (r.getStone() < resource.getStone())
			return false;
		if (r.getCoins() < resource.getCoins())
			return false;
		if (r.getServants() < resource.getServants())
			return false;
		if (r.getFaith() < resource.getFaith())
			return false;
		if (r.getMilitary() < resource.getMilitary())
			return false;
		if (r.getVictory() < resource.getVictory())
			return false;
		return true;
	}

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

	public Resource diffResource(Resource s1, Resource s2) {
		Integer stone = (s1.getStone() - s2.getStone());
		Integer wood = (s1.getWood() - s2.getWood());
		Integer coins = (s1.getCoins() - s2.getCoins());
		Integer servants = (s1.getServants() - s2.getServants());
		Integer military = (s1.getMilitary() - s2.getMilitary());
		Integer faith = (s1.getFaith() - s2.getFaith());
		Integer victory = (s1.getVictory() - s2.getVictory());
		return new Resource(wood, stone, servants, coins, faith, military, victory);
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

	public Resource resourceMultiplication(Resource r, Integer m) {
		Resource bonus = new Resource(0, 0, 0, 0, 0, 0, 0);
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
		bonus.setMilitary(military);
		;
		Integer victory = r.getVictory() * m;
		bonus.setVictory(victory);
		return bonus;
	}
}
