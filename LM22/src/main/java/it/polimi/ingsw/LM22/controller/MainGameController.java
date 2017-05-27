package it.polimi.ingsw.LM22.controller;

import it.polimi.ingsw.LM22.model.Game;

public class MainGameController {

	private Integer TIMER_PER_MOVE;
	private Game game;
	private VaticanReportManager vaticanReportManager;
	private TurnInizializator turnInizializator;
	private MoveManager moveManager;
	
	private void vaticanReport(){
		vaticanReportManager.manageVaticanReport();
	}
	
	private void turnInit(){
		turnInizializator.initializeTurn();
	}
	
	/*
	 * permette di gestire tutta la fase di conteggio dei Punti Finali 
	 * */
	private void manageEndGame(){
		
	}
	
	/*
	 * metodo che gestisce l'attribuzione dei punti vittoria finali in base alla classifica dei punti militari
	 */
	private void manageMilitaryStandingPoints(){
		
	}
	
	/*
	 * metodo che gestisce l'attribuzione dei punti vittoria in base al numero di carte
	 * territorio o personaggio che ogni player ha ottenuto 
	 * + attribuzione dei punti vittoria dati dalle VentureCards
	 * --> in questo caso devo fare il controllo su alcuni tipi di 
	 * scomuniche del terzo periodo
	 */
	private void manageVictoryPointDueToCards(){
		
	}
	
	/*
	 * metodo che gestisce l'attribuzione di punti vittoria in base al numero di risorse 
	 * possedute da ogni player (1 punto vittoria per ogni 5 risorse wood-stone-coins-servants
	 * del player contate tutte insieme )
	 * --> qui controllo la presenza di scomuniche del terzo periodo che non fanno prendere certi punti
	 */
	private void manageVictoryPointsDueToResource(){
		
	}
	
	
	private void electWinner(){
		
	}
	
}
