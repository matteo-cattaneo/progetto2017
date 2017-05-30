package it.polimi.ingsw.LM22.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CardSpace;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.DevelopmentCard;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Resource;

public class MoveManager {

	private final Integer TERRITORY = 0;
	private final Integer CHARACTER = 1;
	private final Integer BUILDING = 2;
	private final Integer VENTURE = 3;
	private Game game = null;
	private ResourceHandler resourceHandler = new ResourceHandler();
	public EffectManager effectManager;

	public MoveManager(Game game) {
		this.game = game;
	}

	public void manageMove(AbstractMove move) {
		boolean checkResult = false;
		String name;
		Method method;
		if (move instanceof MemberMove){
			if (((MemberMove)move).getMemberUsed().isUsed())
				//dobbiamo segnalare al giocatore che la mossa non è valida
				//--> InvalidMoveException ?
				return;
		}
		try {
			name = move.getClass().getSimpleName().toLowerCase() + "Allowed";
			method = this.getClass().getMethod(name, new Class[] { move.getClass() });
			checkResult = (boolean) method.invoke(this, new Object[] { move });
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
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
		else 
			// throw new InvalidMoveException
			return;
	}

	/*
	 * controlla se una mossa del tipo CardMove è ammessa o no
	 */
	private boolean cardmoveAllowed(CardMove cardMove) {
		if (!checkCardSpace(cardMove))
			return false;
		// check se la torre è già occupata da un familiare dello stesso player
		if (game.getBoardgame().getTowers()[cardMove.getTowerSelected()].getColoredMembersOnIt()
				.contains(cardMove.getPlayer().getColor()))
			return false;
		if (!checkCardCost(cardMove))
			return false;
		return false;
	}

	private boolean checkCardCost(CardMove cardMove) {
		int tower = cardMove.getTowerSelected();
		Tower t = game.getBoardgame().getTowers()[tower];
		int floor = cardMove.getLevelSelected();
		DevelopmentCard card = game.getBoardgame().getTowers()[tower].getFloor()[floor].getCard();
		//dobbiamo controllare se ill player non ha la carta Leader delle 3 monete in più
		// ATTENZIONE
		if (t.isOccupied()) {
			switch (tower) {
			// case 3: if
			// (game.getBoardgame().getTowers()[cardMove.getTowerSelected()].getFloor().)
			case 2: if (!resourceHandler.enoughResources((BuildingCard)card, t)) {
					return false;
				}
			case 1: if (!resourceHandler.enoughResources((CharacterCard)card,t)) {
					return false;
				}
			case 0: if (!resourceHandler.enoughResources((TerritoryCard)card,t)) {
					return false;
				}
			}

		}
	}

	private boolean checkCardSpace(CardMove cardMove) {
		CardSpace space = searchCardSpace(cardMove.getTowerSelected(), cardMove.getLevelSelected());
		if (space.getSpaceRequirement() > (cardMove.getMemberUsed().getValue()
				+ cardMove.getServantsAdded().getServants()))
			return false;
		/*
		 * questo controllo deve essere evitato se il Player ha la carta
		 * LUDOVICO ARIOSTO ATTIVATO
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

	/*
	 * gestisce una mossa di tipo CardMove
	 */
	private void cardmoveHandle(CardMove cardMove) {

	}


	/*
	 * controlla se una mossa del tipo MarketMove è ammessa o no
	 */
	private boolean marketmoveAllowed(MarketMove marketMove) {
		//check per controllare se ho scomunica del tipo NoMarkeEx
		int pos = marketMove.getMarketSpaceSelected();
		if (game.getBoardgame().getMarket()[pos].getMember() != null)
			return false;
		if (marketMove.getMemberUsed().getValue() < game.getBoardgame().getMarket()[pos].getSpaceRequirement())
			return false;
		return true;
	}

	/*
	 * gestisce una mossa del tipo MarketMove
	 */
	private void marketmoveHandle(MarketMove marketMove) {
		int opt = marketMove.getMarketSpaceSelected();
		game.getBoardgame().getMarket()[opt].setMember(marketMove.getMemberUsed());
		marketMove.getMemberUsed().setUsed(true);
		/*
		 * prendo le varie risorse (i vari premi contenuti nei MarketSpace)
		 */
		resourceHandler.addResource(marketMove.getPlayer().getPersonalBoard().getResources(), game.getBoardgame().getMarket()[opt].getReward());
		resourceHandler.selectCouncilPrivilege(game.getBoardgame().getMarket()[opt].getCouncilPrivilege());
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
	private boolean councilmoveAllowed(CouncilMove councilMove) {
		if (councilMove.getMemberUsed().getValue() < game.getBoardgame().getCouncilPalace().getSpaceRequirement())
			return false;
		return true;
	}

	/*
	 * gestisce una mossa del tipo CouncilMove
	 */
	private void councilMoveHandle(CouncilMove councilMove) {
		councilMove.getMemberUsed().setUsed(true);
		game.getBoardgame().getCouncilPalace().getMembers().add(councilMove.getMemberUsed());
		/*
		 * prendo le varie risorse (moneta oppure privilegi del consiglio)
		 */
		resourceHandler.addResource(councilMove.getPlayer().getPersonalBoard().getResources(), game.getBoardgame().getMarket()[opt].getReward());
		resourceHandler.selectCouncilPrivilege(game.getBoardgame().getCouncilPalace().getCouncilPrivilege());
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

	private boolean checkWorkSpace() {

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
