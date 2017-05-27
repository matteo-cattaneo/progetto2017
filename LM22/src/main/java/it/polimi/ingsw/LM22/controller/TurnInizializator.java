package it.polimi.ingsw.LM22.controller;

public class TurnInizializator {
	
	/*
	 * metodo generale che gestisce tutta la parte iniziale di preparazione di un nuovo turno
	 * --> invocherà tutti i metodi minori in grado di svolgere le varie funzionalità richieste
	 * per la preparazione di un turno e la pulizia della Board :
	 * - distribuzione carte (di 4 tipi)
	 * - lancio dei dadi
	 * - calcolo nuovo ordine di turno
	 * - 
	 * */
	public void initializeTurn(){
		setNewPlayersOrder();
		cleanBoardGame();
		retireMembers();
		throwDices();
		distributeDevelopmentCards();
	}

	/*
	 * metodo che ritira tutte le carte non prese
	 */
	public void cleanBoardGame(){

	}	
	
	/*
	 * metodo che consente di distribuire tutte e carte sviluppo
	 */
	public void distributeDevelopmentCards(){
		distributeTerritoryCards();
		distributeCharacterCards();
		distributeBuildingCards();
		distributeVentureCards();
	}
	
	public void distributeTerritoryCards(){
		
	}
	
	public void distributeCharacterCards(){
		
	}
	
	public void distributeBuildingCards(){
		
	}
	
	public void distributeVentureCards(){
		
	}
	
	/*
	 * metodo che reinizializza i valori dei dadi
	 */
	public void throwDices(){
		
	}
	
	/*
	 * metodo che presi in ingresso l'ordine del turno appena finito e la lista 
	 * dei famiiari inseriti nel CouncilSpace e genera l'ordine per il nuovo turno
	 */
	public void setNewPlayersOrder(){
		
	}
	
	/*
	 * metodo che si occupa di riportare tutti i familiari al Player
	 */
	public void retireMembers(){
		
	}
}
