package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CardSpace;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.DevelopmentCard;
import it.polimi.ingsw.LM22.model.Resource;
import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;

/*
 * tutta questa classe dovrebbe essere rivista in base al tipo di 
 * struttura che vogliamo dare alla classe Resource del Model
 * - 7 attributi
 * - array di 7 interi 
 * - Set di risorse singole?
 * - altre opzioni?
 * 
 * */

public class ResourceHandler {
	/*
	 * in questa classe bisognerà tenere conto: degli effetti che danno sia
	 * risorse maggiori --> (vedi santa Rita) oppure delle scomuniche che
	 * tolgono risorse...
	 * 
	 */

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
	 * metodo che controlla, solo con interi essendo carte Character,
	 * se il player in grado di comprare una carta: 
	 * - è in grado di pagare le possibili 3 monete in più senza bonus,
	 * - è in grado di pagare il costo della carta anche con i bonus
	 * --> se entrambe le condizioni precedente sono vere allora può effettuare la mossa
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

	public boolean enoughResources(BuildingCard card, CardMove cardMove, Resource additionalCost, Resource bonus) {

	}

	public boolean enoughResources(VentureCard card, CardMove cardMove, Resource additionalCost, Resource bonus) {

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
		// TO-DO
	}

	/*
	 * toglie la risorsa ricevuta come secondo parametro alla risorsa ricevuta
	 * come primo parametro
	 */
	public void subResource(Resource playerResource, Resource addingResource) {
		// TO-DO
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
		stone= s1.getStone() + s2.getStone();
		coins= s1.getCoins() + s2.getCoins();
	    servants = s1.getServants() + s2.getServants();
		faith = s1.getFaith() + s2.getFaith();
		military = s1.getMilitary() + s2.getMilitary();
		victory = s1.getVictory() + s2.getVictory();
		return new Resource(wood, stone, servants, coins, faith, military, victory);
	}

//	public Resource diffResource(Resource s1, Resource s2) {
//		if (enoughResources(s1, s2)) {
//			Integer wood = 0;
//			Integer stone = 0;
//			Integer coins = 0;
//			Integer servants = 0;
//			Integer faith = 0;
//			Integer military = 0;
//			Integer victory = 0;
//			wood = s1.getWood() - s2.getWood();
//			wood = s1.getStone() - s2.getStone();
//			wood = s1.getCoins() - s2.getCoins();
//			wood = s1.getServants() - s2.getServants();
//			wood = s1.getFaith() - s2.getFaith();
//			wood = s1.getMilitary() - s2.getMilitary();
//			wood = s1.getVictory() - s2.getVictory();
//			return new Resource(wood, stone, servants, coins, faith, military, victory);
//		}
//		else return s1;
//	}

	/*
	 * metodo che viene invocato ogni volta che ottengo un effetto comprendente
	 * x councilPrivilege e permette di scegliere x councilPrivilege diversi, si
	 * avrà un ciclo che permette di scegliere tra le varie possibilità e al
	 * ciclo dopo si toglie il tipo di risorsa già scelto
	 */
	public void selectCouncilPrivilege(Integer councilNumber) {
		// TO-DO
	}

}
