package it.polimi.ingsw.LM22.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import it.polimi.ingsw.LM22.model.TerritoryCard;
import it.polimi.ingsw.LM22.model.Tower;
import it.polimi.ingsw.LM22.model.VentureCard;
import it.polimi.ingsw.LM22.model.leader.InOccupiedSpaceEffect;
import it.polimi.ingsw.LM22.model.leader.NoMilitaryRequestEffect;
import it.polimi.ingsw.LM22.model.BuildingCard;
import it.polimi.ingsw.LM22.model.CardSpace;
import it.polimi.ingsw.LM22.model.CharacterCard;
import it.polimi.ingsw.LM22.model.ColorCardBonusEffect;
import it.polimi.ingsw.LM22.model.DevelopmentCard;
import it.polimi.ingsw.LM22.model.Game;
import it.polimi.ingsw.LM22.model.Resource;

public class MoveManager {

	private final Integer SINGLE_PRIVILEGE = 1;
	private final Resource NOTHING = new Resource(0, 0, 0, 0, 0, 0, 0);
	private final Resource THREE_COINS = new Resource(0, 0, 0, 3, 0, 0, 0);
	private final String PRODUCTION = "PRODUCTION";
	private final String HARVEST = "HARVEST";
	private final Integer WORK_MALUS = 3;
	private final Integer THIRD_TERRITORY = 3;
	private final Integer FOURTH_TERRITORY = 7;
	private final Integer FIFTH_TERRITORY = 12;
	private final Integer SIXTH_TERRITORY = 18;
	private final Integer MAX_NUM_CARDS = 6;
	// private final Integer TERRITORY = 0;
	// private final Integer CHARACTER = 1;
	// private final Integer BUILDING = 2;
	// private final Integer VENTURE = 3;
	private Game game = null;
	private ResourceHandler resourceHandler = new ResourceHandler();
	public EffectManager effectManager;

	public MoveManager(Game game) {
		this.game = game;
	}

	/*
	 * metodo che utilizzando la reflection chiama il metodo di check giusto e
	 * successivamente (se il check dà esito positivo) anche il metodo di manage
	 * della mossa giusto
	 */
	public void manageMove(AbstractMove move) {
		boolean checkResult = false;
		String name;
		Method method;
		if (move instanceof MemberMove) {
			if (((MemberMove) move).getMemberUsed().isUsed())
				// dobbiamo segnalare al giocatore che la mossa non è valida
				// --> InvalidMoveException ?
				return;
		}
		try {
			name = move.getClass().getSimpleName().toLowerCase() + "Allowed";
			method = this.getClass().getMethod(name, new Class[] { move.getClass() });
			checkResult = (boolean) method.invoke(this, new Object[] { move });
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {

		}
		if (checkResult) {
			try {
				name = move.getClass().getSimpleName().toLowerCase() + "Handle";
				method = this.getClass().getMethod(name, new Class[] { move.getClass() });
				method.invoke(this, new Object[] { move });
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

			}
		} else
			// throw new InvalidMoveException
			return;
	}

	/*
	 * controlla se una mossa del tipo CardMove è ammessa o no
	 */
	private boolean cardmoveAllowed(CardMove cardMove) {
		if (!checkMaxNumCardLimit(cardMove))
			return false;
		if (!checkCardSpace(cardMove))
			return false;
		// se sono qui HO GIA' RICEVUTO IL BONUS DELLO SPAZIO AZIONE
		// check se la torre è già occupata da un familiare dello stesso player
		if (game.getBoardgame().getTowers()[cardMove.getTowerSelected()].getColoredMembersOnIt()
				.contains(cardMove.getPlayer().getColor()))
			return false;
		if (!checkCardCost(cardMove))
			return false;
		return true;
	}

	/*
	 * controlla se ho già sei carte di quel tipo nella PersonalBoard
	 */
	private boolean checkMaxNumCardLimit(CardMove cardMove) {
		switch (cardMove.getTowerSelected()) {
		case 0:
			if (cardMove.getPlayer().getPersonalBoard().getTerritoriesCards().size() == MAX_NUM_CARDS)
				return false;
			break;
		case 1:
			if (cardMove.getPlayer().getPersonalBoard().getCharactersCards().size() == MAX_NUM_CARDS)
				return false;
			break;
		case 2:
			if (cardMove.getPlayer().getPersonalBoard().getBuildingsCards().size() == MAX_NUM_CARDS)
				return false;
			break;
		case 3:
			if (cardMove.getPlayer().getPersonalBoard().getVenturesCards().size() == MAX_NUM_CARDS)
				return false;
			break;
		}
		return true;
	}

	/*
	 * esegue una serie di controlli sul posizionamento del familiare e se lo
	 * posso mettere guadagno l'ipotetico bonus relativo allo specifico spazio
	 * azione
	 */
	private boolean checkCardSpace(CardMove cardMove) {
		CardSpace space = searchCardSpace(cardMove.getTowerSelected(), cardMove.getLevelSelected());
		if (space.getSpaceRequirement() > (cardMove.getMemberUsed().getValue()
				+ cardMove.getServantsAdded().getServants()))
			return false;
		if (space.getMember() != null)
			if (!hasLudovicoAriostoActivated(cardMove))
				return false;
		Resource bonus = space.getReward();
		resourceHandler.addResource(cardMove.getPlayer().getPersonalBoard().getResources(), bonus);
		return true;
	}

	private boolean checkCardCost(CardMove cardMove) {
		int tower = cardMove.getTowerSelected();
		Tower t = game.getBoardgame().getTowers()[tower];
		int floor = cardMove.getLevelSelected();
		DevelopmentCard card = game.getBoardgame().getTowers()[tower].getFloor()[floor].getCard();
		CardSpace space = searchCardSpace(cardMove.getTowerSelected(), cardMove.getLevelSelected());
		Resource bonus = space.getReward();
		resourceHandler.addResource(cardMove.getPlayer().getPersonalBoard().getResources(), bonus);
		// dobbiamo controllare se il player non ha
		// la carta Leader delle 3 monete in più
		// ATTENZIONE
		Resource additionalCost = NOTHING;
		boolean occupied = t.isOccupied();
		boolean hasBrunelleschi = hasFilippoBrunelleschiActivated(cardMove);
		if (occupied && !hasBrunelleschi)
			additionalCost = THREE_COINS;
		else if (!occupied || hasBrunelleschi)
			additionalCost = NOTHING;
		Resource cardCost = NOTHING;
		switch (tower) {
		case 3:
			// problema sarebbe gestire il doppio costo
			// --> se valido solo 1 faccio comunque la mossa
			// se invece doppio devo chiedere al'utente quale vuole utilizzare
			resourceHandler.manageVentureCost(cardMove);
			break;
		case 2:
			//aggiungere controlli su effetti permanenti per riduzione costo carte
			ColorCardBonusEffect e = new ColorCardBonusEffect();
			if (cardMove.getPlayer().getEffects().contains(e.getCardType()=="BUILDING" && e.getCardDiscount()!= null))
			cardCost = ((BuildingCard) card).getCost();
			if (!resourceHandler.enoughResources(cardCost, cardMove, additionalCost, bonus))
				return false;
			break;
		case 1:
			//aggiungere controlli su effetti permanenti per riduzione costo carte
			cardCost = ((CharacterCard) card).getCost();
			if (!resourceHandler.enoughResources(cardCost, cardMove, additionalCost, bonus))
				return false;
			break;
		case 0:
			cardCost = NOTHING;
			if (!militaryPointsAvailable(cardMove) || !resourceHandler.enoughResources(cardCost, cardMove, additionalCost, bonus))
				return false;
			break;
		}
//		ISTRUZIONE DA ESEGUIRE NELL'HANDLE
//		resourceHandler.addResource(cardMove.getPlayer().getPersonalBoard().getResources(), bonus);
		return true;
	}

	/*
	 * controlla (solo per una CardMove per una TERRITORY) se il player soddisfa
	 * i requisiti relativi ai punti militari --> controlla anche se è stata
	 * attivata la carta Leader che annulla questo controllo
	 */
	private boolean militaryPointsAvailable(CardMove cardMove) {
		if (!hasCesareBorgiaActivated(cardMove)) {
			switch (cardMove.getPlayer().getPersonalBoard().getTerritoriesCards().size()) {
			case 0:
			case 1:
				return true;
			case 2:
				if (cardMove.getPlayer().getPersonalBoard().getResources().getMilitary() < THIRD_TERRITORY)
					return false;
				return true;
			case 3:
				if (cardMove.getPlayer().getPersonalBoard().getResources().getMilitary() < FOURTH_TERRITORY)
					return false;
				return true;
			case 4:
				if (cardMove.getPlayer().getPersonalBoard().getResources().getMilitary() < FIFTH_TERRITORY)
					return false;
				return true;
			case 5:
				if (cardMove.getPlayer().getPersonalBoard().getResources().getMilitary() < SIXTH_TERRITORY)
					return false;
				return true;
			default:
				return false;
			}
		}
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
		// check per controllare se ho scomunica del tipo NoMarkeEx
		int pos = marketMove.getMarketSpaceSelected();
		if (game.getBoardgame().getMarket()[pos].getMember() != null)
			if (!hasLudovicoAriostoActivated(marketMove))
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
		resourceHandler.addResource(marketMove.getPlayer().getPersonalBoard().getResources(),
				game.getBoardgame().getMarket()[opt].getReward());
		resourceHandler.selectCouncilPrivilege(game.getBoardgame().getMarket()[opt].getCouncilPrivilege());
	}

	/*
	 * controlla se una mossa del tipo WorkMove è ammessa o no
	 */
	private boolean workMoveAllowed(WorkMove workMove) {
		return checkWorkSpace(workMove);
	}

	/*
	 * controlla se il valore del familiare utilizzato + servitori soddisfa il
	 * requisito relativo allo spazio azione selezionato per la mossa + se il
	 * primo spazio è già occupato effettua una diminuzione del valore
	 * dell'azione in base al Malus specificato nelle regole
	 */
	private boolean checkWorkSpace(WorkMove workMove) {
		if (!hasLudovicoAriostoActivated(workMove)) {
			switch (game.getPlayers().length) {
			case 2:
				if (workMove.getWorkType() == "PRODUCTION") {
					if (!game.getBoardgame().getProductionSpace().getMembers().isEmpty())
						return false;
					break;
				} else {
					if (!game.getBoardgame().getHarvestSpace().getMembers().isEmpty())
						return false;
					break;
				}
			case 3:
			case 4:
				if (workMove.getWorkType() == "PRODUCTION") {
					if (game.getBoardgame().getProductionSpace().getColoredMemberOnIt()
							.contains(workMove.getPlayer().getColor()))
						return false;
					if (!game.getBoardgame().getProductionSpace().getMembers().isEmpty())
						workMove.getMemberUsed().setValue(workMove.getMemberUsed().getValue() - WORK_MALUS);
					break;
				} else {
					if (game.getBoardgame().getHarvestSpace().getColoredMemberOnIt()
							.contains(workMove.getPlayer().getColor()))
						return false;
					if (!game.getBoardgame().getHarvestSpace().getMembers().isEmpty())
						workMove.getMemberUsed().setValue(workMove.getMemberUsed().getValue() - WORK_MALUS);
					break;
				}
			default:
				return false;
			}
		}
		return true;
	}

	/*
	 * gestisce una mossa del tipo WorkMove
	 */
	private void workMoveHandle(WorkMove workMove) {

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
		game.getBoardgame().getCouncilPalace().getMembers()
				.set(game.getBoardgame().getCouncilPalace().getMembers().size(), councilMove.getMemberUsed());
		/*
		 * prendo le varie risorse (moneta oppure privilegi del consiglio)
		 */
		resourceHandler.addResource(councilMove.getPlayer().getPersonalBoard().getResources(),
				game.getBoardgame().getCouncilPalace().getReward());
		resourceHandler.selectCouncilPrivilege(game.getBoardgame().getCouncilPalace().getCouncilPrivilege());
	}

	/*
	 * gestisce la vendita di una carta leader e consentirà la scelta del
	 * privilegio del consiglio + se la carta era attiva devo anche togliere il
	 * suo effetto dalla lista degli effetti attualmente attivi
	 */
	private void sellLeaderCard() {
		// controllo se la carta è già stata venduta o meno
		// + cancello il suo effetto dalla lista degli effetti attivi (se
		// presente)
		resourceHandler.selectCouncilPrivilege(SINGLE_PRIVILEGE);
	}

	/*
	 * indica una carta leader come attivata nel caso i suoi requisiti siano
	 * soddisfatti --> aggiunge il relativo effetto alla lista degli effetti
	 * presente nel Player
	 */
	private void activateLeaderCard(LeaderCardActivation move) {
		if (leaderCardActivationAllowed(move))
			move.getPlayer().getEffects().set(move.getPlayer().getEffects().size(), move.getLeaderCard().getEffect());
	}

	/*
	 * controlla se i requisiti della carta leader che il player vuole attivare
	 * sono effettivmanete soddisfatti e nel caso indica quella carta come
	 * attivata --> sarà poi gestita dai vari controlli comunque interni a
	 * questa classe
	 */
	private boolean leaderCardActivationAllowed(LeaderCardActivation move) {
		return true;// TODO
	}

	/*
	 * scorre la lista degli effetti attualmente attivi e cerca Ludovico Ariosto
	 * (attivo)
	 */
	private boolean hasLudovicoAriostoActivated(AbstractMove move) {
		InOccupiedSpaceEffect e = new InOccupiedSpaceEffect();
		/*
		 * qui vorrei avere una istruzione che mi cerca se ho un elemento di
		 * tipo InOccupiedSpaceEffect TODO
		 */
		boolean res = move.getPlayer().getEffects().contains(e.getClass());
		res = move.getPlayer().getEffects().contains(InOccupiedSpaceEffect.class);
		return res;
	}

	/*
	 * scorre la lista degli effetti attualmente attivi e cerca Cesare Borgia
	 * (attivo)
	 */
	private boolean hasCesareBorgiaActivated(CardMove move) {
		NoMilitaryRequestEffect e = new NoMilitaryRequestEffect();
		/*
		 * qui vorrei avere una istruzione che mi cerca se ho un elemento di
		 * tipo NoMilitaryRequestEffect TODO
		 */
		boolean res = move.getPlayer().getEffects().contains(e.getClass());
		res = move.getPlayer().getEffects().contains(NoMilitaryRequestEffect.class);
		return res;
	}

	private boolean hasFilippoBrunelleschiActivated(CardMove move) {
		return true; // TODO
	}

}
