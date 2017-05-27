package it.polimi.ingsw.LM22.controller;

public class MoveManager {

	public EffectManager effectManager;
		
	/*
	 * gestisce lo spostamento del familiare, il settaggio degli spazi, torri etc
	 */
	private void moveMember(){
		
	}
	
	/*
	 * gestisce una mossa di tipo CardMove
	 */
	private void cardMoveHandle(){
		
	}
	
	/*
	 * gestisce una mossa del tipo MarketMove
	 */
	private void marketMoveHandle(){
		
	}
	
	/*
	 * gestisce una mossa del tipo WorkMove
	 */
	private void workMoveHandle(){
		
	}
	
	/*
	 * gestisce una mossa del tipo CouncilMove
	 */
	private void councilMoveHandle(){
		
	}
	
	/*
	 * controlla se una mossa del tipo CardMove è ammessa o no
	 */
	private boolean cardMoveAllowed(){
		
	}
	
	/*
	 * controlla se una mossa del tipo MarketMove è ammessa o no
	 */
	private boolean marketMoveAllowed(){
		
	}
	
	/*
	 * controlla se una mossa del tipo WorkMove è ammessa o no
	 */
	private boolean workMoveAllowed(){
		
	}
	
	/*
	 * controlla se una mossa del tipo CouncilMove è ammessa o no
	 */
	private boolean councilMoveAllowed(){
		
	}
	
	/*
	 * metodo che si occupa della gestione di una azione di produzione
	 * --> devo sempre controllare le carte del player singolarmente
	 * ma solo alla fine della produzione darò tutti i bonus al player
	 * perchè alcuni effetti vanno attivati solo con le risorse già 
	 * possedute dal player
	 * --> bisogna sempre chiedere al player se vuole o no ottenere tipo il cambio risorse
	 * e anche chiedere quale attivare nel caso di DoubleChangeEffect
	 */
	private void productionHandle(){
		
	}
	
	/*
	 * gestisce la fase di Raccolto
	 */
	private void harvestHandle(){
		
	}
	
	
	/*
	 * controlla se il valore del familiare utilizzato + servitori soddisfa il requisito
	 * relativo allo spazio azione selezionato per la mossa
	 */
	private boolean checkSpace(){
		
	}
	
	/*
	 * controlla se la torre è occupata e aggiunge il costo delle 3 monete 
	 * all'ipotetico costo della carta che il player vuole prendere
	 * --> controlla se il giocatore ha attivato la Carta Leader specifica 
	 * per questa regola
	 * --> deve essere controllato prima di ricevere il bonus perchè le monete
	 * che ricevo dagli spazi azione non possono essere utilizzati per 
	 * pagare questo costo aggiuntivo
	 */
	private void manageOccupiedTower(){
		
	}
	
	/*
	 * controlla se il player ha risorse necessarie per pagare il costo della carta
	 * --> gestisce anche la somma dei bonus degli spazi azione e relativa sottrazione
	 * nel caso in cui una mossa non sia ammissibile
	 */
	private boolean cardCostAvailable(){
		
	}
	
	/*
	 * controlla (solo per una CardMove per una TERRITORY) se il player soddisfa i requisiti 
	 * relativi ai punti militari 
	 * --> controlla anche se è stata attivata la carta Leader che annulla questo controllo
	 */
	private boolean militaryPointsAvailable(){
		
	}
	
	/*
	 * gestisce la vendita di una carta leader e consentirà la scelt a del privilegio del consiglio
	 */
	private void sellLeaderCard(){
		
	}
	
	/*
	 * indica una carta leader come attivata nel caso i suoi requisiti siano soddisfatti
	 */
	private void activateLeaderCard(){
		
	}
	
	/*
	 * controlla se i requisiti della carta leader che il player vuole
	 * attivare sono effettivmanete soddisfatti e nel caso indica quella carta 
	 * come attivata --> sarà poi gestita dai vari controlli comunque interni
	 * a questa classe
	 */
	private boolean leaderCardActivationAllowed(){
		
	}	
	
}
