package it.polimi.ingsw.LM22.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import it.polimi.ingsw.LM22.model.CardSpace;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Resource;

public class MoveManager {

	private final String[] TOWERS = {"TERRITORY", "CHARACTER", "BUILDING", "VENTURE"};
	private Game game = null;
	
	public MoveManager(Game game){
		this.game = game;
	}
	
	private ResourceHandler resourceHandler = new ResourceHandler();
	public EffectManager effectManager;

	public void manageMove(AbstractMove move) {
		boolean checkResult = false;
		String name;
		Method method;
		try {
			name = move.getClass().getSimpleName().toLowerCase() + "Allowed";
			method = this.getClass().getMethod(name, new Class[] { move.getClass() });
			checkResult = (boolean) method.invoke(this, new Object[] { move });
		} catch (NoSuchMethodException | IllegalAccessException	| InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (checkResult) {
			try {
				name = move.getClass().getSimpleName().toLowerCase() + "Handle";
				method = this.getClass().getMethod(name, new Class[] { move.getClass() });
				method.invoke(this, new Object[] { move });
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * gestisce lo spostamento del familiare, il settaggio degli spazi, torri
	 * etc
	 */
	private void moveMember() {

	}

	/*
	 * controlla se una mossa del tipo CardMove è ammessa o no
	 */
	private boolean cardmoveAllowed(CardMove cardMove) {

	}

	/*
	 * gestisce una mossa di tipo CardMove
	 */
	private void cardmoveHandle(CardMove cardMove) {

	}

	/*
	 * controlla se una mossa del tipo MarketMove è ammessa o no
	 */
	private boolean marketMoveAllowed(MarketMove marketMove) {

	}

	/*
	 * gestisce una mossa del tipo MarketMove
	 */
	private void marketMoveHandle(MarketMove marketMove) {

	}

	/*
	 * controlla se una mossa del tipo WorkMove è ammessa o no
	 */
	private boolean workMoveAllowed(WorkMove workMove) {

	}

	/*
	 * gestisce una mossa del tipo WorkMove
	 */
	private void workMoveHandle(WorkMove workMove) {

	}

	/*
	 * controlla se una mossa del tipo CouncilMove è ammessa o no
	 */
	private boolean councilMoveAllowed(CouncilMove councilMove) {

	}

	/*
	 * gestisce una mossa del tipo CouncilMove
	 */
	private void councilMoveHandle(CouncilMove councilMove) {

	}

	/*
	 * metodo che si occupa della gestione di una azione di produzione --> devo
	 * sempre controllare le carte del player singolarmente ma solo alla fine
	 * della produzione darò tutti i bonus al player perchè alcuni effetti vanno
	 * attivati solo con le risorse già possedute dal player --> bisogna sempre
	 * chiedere al player se vuole o no ottenere tipo il cambio risorse e anche
	 * chiedere quale attivare nel caso di DoubleChangeEffect
	 */
	private void productionHandle() {

	}

	/*
	 * gestisce la fase di Raccolto
	 */
	private void harvestHandle() {

	}

	/*
	 * controlla se il valore del familiare utilizzato + servitori soddisfa il
	 * requisito relativo allo spazio azione selezionato per la mossa
	 */
	private boolean checkCardSpace(CardMove cardMove) {
		CardSpace space = searchCardSpace(cardMove.getTowerSelected(), cardMove.getLevelSelected());
		if (space.getSpaceRequirement()>(cardMove.getMemberUsed().getValue()+cardMove.getServantsAdded().getServants()))
			return false;
		/*
		 * questo controllo deve essere evitato se il Player ha la carta LUDOVICO ARIOSTO ATTIVATO
		 */
		if (space.getMember() != null)
			return false;
		Resource bonus = space.getReward();
		resourceHandler.addResource(cardMove.getPlayer().getPersonalBoard().getResources(), bonus);
		return true;
	}

	private CardSpace searchCardSpace(Integer towerSelected, Integer levelSelected) {
		return game.getBoardgame().getTowers()[towerSelected].getFloor()[levelSelected].getSpace();
	}

	private boolean checkMarketSpace() {

	}

	private boolean checkWorkSpace() {

	}

	private boolean checkCouncilSpace() {

	}

	/*
	 * controlla se la torre è occupata e aggiunge il costo delle 3 monete
	 * all'ipotetico costo della carta che il player vuole prendere -->
	 * controlla se il giocatore ha attivato la Carta Leader specifica per
	 * questa regola --> deve essere controllato prima di ricevere il bonus
	 * perchè le monete che ricevo dagli spazi azione non possono essere
	 * utilizzati per pagare questo costo aggiuntivo
	 */
	private void manageOccupiedTower() {

	}

	/*
	 * controlla se il player ha risorse necessarie per pagare il costo della
	 * carta --> gestisce anche la somma dei bonus degli spazi azione e relativa
	 * sottrazione nel caso in cui una mossa non sia ammissibile
	 */
	private boolean cardCostAvailable() {

	}

	/*
	 * controlla (solo per una CardMove per una TERRITORY) se il player soddisfa
	 * i requisiti relativi ai punti militari --> controlla anche se è stata
	 * attivata la carta Leader che annulla questo controllo
	 */
	private boolean militaryPointsAvailable() {

	}

	/*
	 * gestisce la vendita di una carta leader e consentirà la scelt a del
	 * privilegio del consiglio
	 */
	private void sellLeaderCard() {

	}

	/*
	 * indica una carta leader come attivata nel caso i suoi requisiti siano
	 * soddisfatti
	 */
	private void activateLeaderCard() {

	}

	/*
	 * controlla se i requisiti della carta leader che il player vuole attivare
	 * sono effettivmanete soddisfatti e nel caso indica quella carta come
	 * attivata --> sarà poi gestita dai vari controlli comunque interni a
	 * questa classe
	 */
	private boolean leaderCardActivationAllowed() {

	}

}
