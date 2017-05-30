package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.BuildingCard;
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
	public boolean enoughResources(TerritoryCard card, CardMove cardMove) {
		
	}

	public boolean enoughResources(CharacterCard card, CardMove cardMove) {

	}

	public boolean enoughResources(BuildingCard card, CardMove cardMove) {

	}

	public boolean enoughResources(VentureCard card, CardMove cardMove) {

	}

	/*
	 * metodo in grado di controllare se la prima risorsa è >= della seconda
	 * (true)
	 */
	public boolean compareResources(Resource playerResource, Resource resource) {
		// TO-DO
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
